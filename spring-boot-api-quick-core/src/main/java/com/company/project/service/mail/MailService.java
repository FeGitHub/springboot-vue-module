package com.company.project.service.mail;

import com.company.project.master.model.Config;
import com.company.project.service.impl.ConfigServiceImpl;
import com.company.project.utils.DateUtils;
import com.company.project.utils.MailUtils;
import com.company.project.vo.util.mail.ImapEmailInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class MailService {

    private Logger logger = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private ConfigServiceImpl configServiceImpl;

    private Config mainConfig = null;

    private Config instructUserConfig = null;

    private static String instructKey = "指令动作";


    /**
     * 获取今天内最新的信息
     *
     * @return
     */
    public ImapEmailInfo getTodayLastMsg() {
        if (mainConfig == null) {
            mainConfig = configServiceImpl.findBy("configtype", "mail");
            MailUtils.setReceiveMailConfig(mainConfig);
        }
        List<ImapEmailInfo> result = MailUtils.receiveMail(Boolean.FALSE);//只找未读的
        if (result.size() > 0) {
            Comparator<ImapEmailInfo> byReceivedDateDesc = Comparator.comparing(ImapEmailInfo::getReceivedDate).reversed();
            result.sort(Comparator.nullsLast(byReceivedDateDesc));
        } else {
            // logger.info("不存在未读的最新信息");
        }
        return result.stream().findFirst().orElse(null);
    }

    /***
     * 根据邮件做出对应的动作
     * @param email
     */
    public void doByMail(ImapEmailInfo email) {
        List<String> readMailList = new ArrayList<>();
        if (instructUserConfig == null) {
            instructUserConfig = configServiceImpl.findBy("configtype", "instructMail");
        }
        if (email != null && instructUserConfig != null) {
            //如果邮件的接收时间是当天
            if (DateUtils.sameDate(new Date(), email.getReceivedDate())) {
                //并且发送人是指定的邮箱用户
                if (instructUserConfig.getPropval1().equals(email.getSender())) {
                    //正文包含关键字
                    if (email.getContent().indexOf(instructKey) > -1) {
                        System.out.println("做出指令动作...");
                        System.out.println(email.getSubject());
                        System.out.println(email.getSender());
                        System.out.println(email.getContent());
                        readMailList.add(email.getMessageID());
                        MailUtils.readMail(readMailList);
                    }
                }
            }
        }
    }
}
