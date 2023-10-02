package com.company.project.service.mail;

import com.company.project.utils.CmdUtil;
import com.company.project.utils.MailUtils;
import com.company.project.vo.util.mail.ImapEmailInfo;
import com.company.project.vo.util.mail.SendMailVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MailOrderService {

    private List<String> replayKeyList = Arrays.asList("回复邮件", "邮件回复");


    private List<String> closeKeyList = Arrays.asList("电脑关机");

    /***
     * 总控制器
     * @param email
     */
    public void doByOrder(ImapEmailInfo email) {
        replyMail(email);
        shutdown(email);
    }

    /***
     * 邮件回复
     * @param email
     */
    public void replyMail(ImapEmailInfo email) {
        for (String order : replayKeyList) {
            if (email.getContent().indexOf(order) > -1) {
                MailUtils.sendMail(new SendMailVo(email.getSender(), "机器回复", "遵从命令回复邮件"));
            }
        }
    }

    /***
     * 关机
     * @param email
     */
    public void shutdown(ImapEmailInfo email) {
        for (String order : closeKeyList) {
            if (email.getContent().indexOf(order) > -1) {
                CmdUtil.excuteCmdCommand("shutdown -r -t 10");
            }
        }
    }
}
