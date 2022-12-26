package com.test.demo.mapStruct;

import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormtUtil {

    @DateFormat
    public static String dateToString(Date date) {
        return date == null ? "没有哦" : new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    public @interface DateFormat {
    }
}


