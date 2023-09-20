package com.company.project.utils;


public class CronValidateUtils {


    /***
     *检验邮箱是否合法
     * @param email
     * @return
     */
    public static boolean checkEmailFormat(String email) {
        String regex = "^(\\w+([-.][A-Za-z0-9]+)*){1,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";
        return email.toString().matches(regex);
    }

}
