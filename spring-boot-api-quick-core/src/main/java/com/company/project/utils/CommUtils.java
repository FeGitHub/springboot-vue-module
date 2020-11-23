package com.company.project.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 公用组件工具类
 */
public class CommUtils {

    public static Date strToDateYYYMMDD(String strDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date strtodate = formatter.parse(strDate);
        return strtodate;
    }


    public static String  timestampToDateYYYMMDD(Date timestamp) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String    dateStr = sdf.format(timestamp);
         return dateStr;
    }

    /**
     * 创建uuid
     */
    public static String  createUUID ()   {
         String uuid = UUID.randomUUID().toString();  //转化为String对象
         uuid = uuid.replace("-", "");
         return uuid;
    }
}
