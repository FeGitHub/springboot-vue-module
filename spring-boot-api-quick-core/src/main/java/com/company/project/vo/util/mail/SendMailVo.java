package com.company.project.vo.util.mail;

public class SendMailVo {


    public SendMailVo(String email, String subject, String emailMsg) {
        this.email = email;
        this.subject = subject;
        this.emailMsg = emailMsg;
    }


    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailMsg() {
        return emailMsg;
    }

    public void setEmailMsg(String emailMsg) {
        this.emailMsg = emailMsg;
    }

    private String subject;
    private String emailMsg;
}
