package com.company.project.utils;

public class DesConfig {

    private String username;
    private String sepChar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSepChar() {
        return sepChar;
    }

    public void setSepChar(String sepChar) {
        this.sepChar = sepChar;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getDesKey() {
        return desKey;
    }

    public void setDesKey(String desKey) {
        this.desKey = desKey;
    }

    private String secretKey;
    private String desKey;

}
