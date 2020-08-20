package com.company.project.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

}
