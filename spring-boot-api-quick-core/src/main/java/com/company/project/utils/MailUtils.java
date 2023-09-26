package com.company.project.utils;

import com.company.project.master.model.Config;
import com.company.project.vo.util.mail.SendMailVo;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/****
 *   邮件服务工具类
 */
public class MailUtils {
    private static String MAIL_ADRESS = "XXX";

    private static String AUTHORIZE_CODE = "XXXXXXXXXXXX";


    public static void main(String[] args) {
        sendMail(new SendMailVo("XXX@qq.com", "标题", "这是提示的信息"));
    }

    /***
     * 重新设置配置信息
     * @param mainConfig
     */
    public static void setConfig(Config mainConfig) {
        MAIL_ADRESS = mainConfig.getPropval1();
        AUTHORIZE_CODE = mainConfig.getPropval2();
    }


    /**
     * @throws AddressException
     * @throws MessagingException
     */
    public static void sendMail(SendMailVo sendMailVo) {
        try {
            //创建配置文件
            Properties props = new Properties();
            //设置发送时遵从SMTP协议
            props.setProperty("mail.transport.protocol", "SMTP");
            props.setProperty("mail.host", "smtp.qq.com");
            //设置用户的认证方式auth
            props.setProperty("mail.smtp.auth", "true");
            Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    //return new PasswordAuthentication("用户名", "密码");
                    //注意qq邮箱需要去qq邮箱的设置中获取授权码，并将授权码作为密码来填写
                    return new PasswordAuthentication(MAIL_ADRESS, AUTHORIZE_CODE);
                }
            };
            //创建session域
            Session session = Session.getInstance(props, auth);
            Message message = new MimeMessage(session);
            //设置邮件发送者,与PasswordAuthentication中的邮箱一致即可
            message.setFrom(new InternetAddress(MAIL_ADRESS));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(sendMailVo.getEmail()));
            //设置邮件主题
            message.setSubject(sendMailVo.getSubject());
            //设置邮件内容
            message.setContent(sendMailVo.getEmailMsg(), "text/html;charset=utf-8");
            //发送邮件
            Transport.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

