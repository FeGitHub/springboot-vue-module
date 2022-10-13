package com.company.project.utils;

import java.util.UUID;

/***
 * uuid工具类
 */
public class UuidUtils {

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
