package com.company.project.job;

import com.company.project.service.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/***
 *  监听收取邮件
 */
@Component
public class ListenReceiveMailTask {
    @Autowired
    private MailService mailService;

    private Logger logger = LoggerFactory.getLogger(ListenReceiveMailTask.class);

    @Scheduled(cron = "${custom.biz.five.second.do.cron}")

    private void process() {
        // logger.info("邮件收集开始");
        //  ImapEmailInfo email = mailService.getTodayLastMsg();
        //  mailService.doByMail(email);
        //  logger.info("邮件收集结束");
       /* if (email != null) {
            System.out.println(email.getSubject());
            System.out.println(email.getSender());
            System.out.println(email.getContent());
            System.out.println(email.getReceivedDate() != null ? DateUtils.formatDate(email.getReceivedDate(), DateUtils.yyyy_MM_dd) : "");
        }*/
    }

}
