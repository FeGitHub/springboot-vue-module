package com.company.project.vo.mail;

import com.company.project.vo.util.mail.ImapEmailInfo;

import java.util.ArrayList;
import java.util.List;

public class GetMailAction {

    public List<ImapEmailInfo> getImapEmailInfoList() {
        return imapEmailInfoList;
    }

    public void setImapEmailInfoList(List<ImapEmailInfo> imapEmailInfoList) {
        this.imapEmailInfoList = imapEmailInfoList;
    }

    List<ImapEmailInfo> imapEmailInfoList = new ArrayList<>();

}
