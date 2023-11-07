package com.company.project.utils;

import com.company.project.master.model.Config;
import com.company.project.vo.mail.DoMailAction;
import com.company.project.vo.mail.GetMailAction;
import com.company.project.vo.util.mail.ImapEmailInfo;
import com.company.project.vo.util.mail.SendMailVo;
import org.springframework.util.StopWatch;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/****
 *   邮件服务工具类
 */
public class MailUtils {


    private static String SEND_MAIL_ADRESS = "";

    private static String SEND_AUTHORIZE_CODE = "";

    private static String RECEIVE_MAIL_ADRESS = "";

    private static String RECEIVE_AUTHORIZE_CODE = "";

    public static void main(String[] args) {
        //sendMail(new SendMailVo("XXXXXXXXXXXX@qq.com", "标题", "这是提示的信息"));
       /* List<ImapEmailInfo> result = receiveMail(null);
        if (result.size() > 0) {
            for (ImapEmailInfo email : result) {
                System.out.println(email.getSubject());
                System.out.println(email.getSender());
                System.out.println(email.getContent());
                System.out.println(email.getReceivedDate() != null ? DateUtils.formatDate(email.getReceivedDate(), DateUtils.yyyy_MM_dd) : "");
            }
        }*/
    }

    /***
     * 重新设置配置发送信息
     * @param mailConfig
     */
    public static void setSendMailConfig(Config mailConfig) {
        SEND_MAIL_ADRESS = mailConfig.getPropval1();
        SEND_AUTHORIZE_CODE = mailConfig.getPropval2();
    }


    /***
     * 重新设置配置接收信息
     * @param mailConfig
     */
    public static void setReceiveMailConfig(Config mailConfig) {
        RECEIVE_MAIL_ADRESS = mailConfig.getPropval1();
        RECEIVE_AUTHORIZE_CODE = mailConfig.getPropval2();
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
                    return new PasswordAuthentication(SEND_MAIL_ADRESS, SEND_AUTHORIZE_CODE);
                }
            };
            //创建session域
            Session session = Session.getInstance(props, auth);
            Message message = new MimeMessage(session);
            //设置邮件发送者,与PasswordAuthentication中的邮箱一致即可
            message.setFrom(new InternetAddress(SEND_MAIL_ADRESS));
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


    /***
     * 找出未读的邮件
     * @param folder
     * @return
     * @throws MessagingException
     */
    public static Message[] findUnReadMessages(Folder folder) throws MessagingException {
        Flags seen = new Flags(Flags.Flag.SEEN);
        FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
        return folder.search(unseenFlagTerm);
    }


    /***
     * 将邮件标记为已读
     * @throws Exception
     */
    public static void readMail(List<String> readMailList) {
        //操作的类型
        DoMailAction doMailAction = new DoMailAction(DoMailAction.readMail);
        //标记已读的对象
        doMailAction.setReadMailList(readMailList);
        doMailOperator(doMailAction);
    }

    /***
     * 接收邮件
     * @throws Exception
     */
    public static List<ImapEmailInfo> receiveMail(Boolean readStatue) {
        StopWatch sw = new StopWatch("接收邮件");
        sw.start("接收邮件开始...");
        DoMailAction doMailAction = new DoMailAction(DoMailAction.receiveMail);
        doMailAction.setReadStatue(readStatue);
        List<ImapEmailInfo> result = doMailOperator(doMailAction).getImapEmailInfoList();
        sw.stop();
        // System.out.printf("接收邮件耗时：%d%s.\n", sw.getLastTaskTimeMillis(), "ms");
        return result;
    }

    /***
     * 操作邮箱
     * @throws Exception
     */
    public static GetMailAction doMailOperator(DoMailAction doMailAction) {
        GetMailAction getMailAction = new GetMailAction();
        try {
            String pop3Server = "smtp.qq.com";
            String protocol = "imap";
            String username = RECEIVE_MAIL_ADRESS;
            String password = RECEIVE_AUTHORIZE_CODE;
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", protocol); // 使用的协议（JavaMail规范要求）
            props.setProperty("mail.smtp.host", pop3Server); // 发件人的邮箱的 SMTP服务器地址
            // 获取连接
            Session session = Session.getDefaultInstance(props);
            session.setDebug(false);
            // 获取Store对象
            Store store = session.getStore(protocol);
            store.connect(pop3Server, username, password); // POP3服务器的登陆认证
            // 通过POP3协议获得Store对象调用这个方法时，邮件夹名称只能指定为"INBOX"
            Folder folder = store.getFolder("INBOX");// 获得用户的邮件帐户
            folder.open(Folder.READ_WRITE); // 设置对邮件帐户的访问权限
            if (DoMailAction.receiveMail.equals(doMailAction.getDoType())) {//接收邮件信息
                Message[] messages = folder.getMessages();// 得到邮箱帐户中的所有邮件;
                if (Boolean.FALSE.equals(doMailAction.getReadStatue())) {
                    messages = findUnReadMessages(folder);
                }
                if (messages != null && messages.length > 0) {
                    getMailAction.setImapEmailInfoList(parseEmail(messages));
                }
            } else if (DoMailAction.readMail.equals(doMailAction.getDoType())) {//标记邮件为可读
                Message[] messages = folder.getMessages();// 得到邮箱帐户中的所有邮件
                if (messages != null && messages.length > 0) {
                    for (Message message : messages) {
                        MimeMessage msg = (MimeMessage) message;
                        if (doMailAction.getReadMailList().indexOf(msg.getMessageID()) > -1) {
                            message.setFlag(Flags.Flag.SEEN, true);
                        }
                    }
                }
            }
            folder.close(false);// 关闭邮件夹对象
            store.close(); // 关闭连接对象
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return getMailAction;
    }


    /**
     * 解析邮件并返回
     *
     * @param messages
     */
    public static List<ImapEmailInfo> parseEmail(Message[] messages) throws Exception {
        List<ImapEmailInfo> result = new ArrayList<>();
        if (messages == null || messages.length == 0) {
            return result;
        }
        for (Message message : messages) {
            MimeMessage msg = (MimeMessage) message;
            ImapEmailInfo emailInfo = new ImapEmailInfo();
            emailInfo.setMessage(message);
            emailInfo.setMessageID(msg.getMessageID());
            if (msg != null && StringUtils.isNotEmpty(msg.getSubject())) {
                emailInfo.setSubject(MimeUtility.decodeText(msg.getSubject()));
            }
            emailInfo.setSender(getSenderAddress(msg));
            emailInfo.setReceivedDate(msg.getReceivedDate());
            StringBuffer content = new StringBuffer();
            String contentType = msg.getContentType();
            if (contentType.toLowerCase().startsWith("text/plain")) {
                getHtmlContent(msg, content, true);
            } else {
                getHtmlContent(msg, content, false);
            }
            emailInfo.setContent(content.toString());
            result.add(emailInfo);
        }
        return result;
    }

    //获取邮件发件人address
    private static String getSenderAddress(MimeMessage msg) throws Exception {
        Address[] froms = msg.getFrom();
        if (froms.length < 1)
            throw new MessagingException("没有发件人!");
        InternetAddress address = (InternetAddress) froms[0];
        if (address != null) {
            return address.getAddress();
        }
        return null;
    }

    //获取邮件内的HTML内容
    private static void getHtmlContent(Part part, StringBuffer content, boolean plainFlag) throws MessagingException, IOException {
        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/html") && !isContainTextAttach && plainFlag == false) {
            content.append(MimeUtility.decodeText(part.getContent().toString()));
        } else if (part.isMimeType("text/plain") && !isContainTextAttach && plainFlag) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getHtmlContent((Part) part.getContent(), content, plainFlag);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getHtmlContent(bodyPart, content, plainFlag);
            }
        }
    }

}

