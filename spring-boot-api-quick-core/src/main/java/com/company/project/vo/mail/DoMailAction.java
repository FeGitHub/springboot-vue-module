package com.company.project.vo.mail;

import java.util.Arrays;
import java.util.List;

public class DoMailAction {

    public DoMailAction(String doType) {
        this.doType = doType;
    }


    public static String receiveMail = "receiveMail";

    public static String readMail = "readMail";

    public static List<String> doTypeList = Arrays.asList(receiveMail, readMail);

    private String doType;

    public List<String> getReadMailList() {
        return readMailList;
    }

    public void setReadMailList(List<String> readMailList) {
        this.readMailList = readMailList;
    }

    public static String getReceiveMail() {
        return receiveMail;
    }

    public static void setReceiveMail(String receiveMail) {
        DoMailAction.receiveMail = receiveMail;
    }

    public String getDoType() {
        return doType;
    }

    public void setDoType(String doType) {
        this.doType = doType;
    }


    public List<String> readMailList;

    private Boolean readStatue;

    public Boolean getReadStatue() {
        return readStatue;
    }

    public void setReadStatue(Boolean readStatue) {
        this.readStatue = readStatue;
    }
}
