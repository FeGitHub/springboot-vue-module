package com.company.project.vo.util.mail;


import lombok.Data;

import javax.mail.Message;
import java.io.Serializable;
import java.util.Date;


@Data
public class ImapEmailInfo implements Serializable {
    private static final long serialVersionUID = 6431149074654460271L;

    private String subject;
    private String sender;
    private String content;
    private Date receivedDate;
    private String messageID;
    private Message message;

}

