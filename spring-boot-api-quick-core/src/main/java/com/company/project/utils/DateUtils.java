package com.company.project.utils;

import org.apache.dubbo.common.utils.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    public static final String yyyy_MM_dd = "yyyy-MM-dd";

    public static final String yyyyMMdd = "yyyyMMdd";

    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public static final String yyyy_MM_dd_HH_mm_ss_chin = "yyyy年MM月dd日HH时mm分ss秒";

    /**
     * 按格式输出string到date
     *
     * @param dateString
     * @param style      格式化参数
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String dateString, String style) {
        try {
            DateFormat theDateFormat = new SimpleDateFormat(style);
            return theDateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("日期转换错误", e);
        }
    }


    /***
     * 按格式输出date到string
     * @param date
     * @param style
     * @return
     */
    public static String formatDate(Date date, String style) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(style).format(date);
    }

    /**
     * 将日期转换成标准格式的字符串<br>
     * 标准格式为<code> yyyy-MM-dd </code>
     *
     * @param date 日期
     * @return 标准格式的字符串
     */
    public static String formatLocalDate(LocalDate date, String style) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(style);
        return date.format(formatter);
    }


    /**
     * 按格式输出string到LocalDateTime
     *
     * @param dateString
     * @param style      格式化参数
     * @return
     * @throws ParseException
     */
    public static LocalDate parseLocalDate(String dateString, String style) {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        try {
            DateFormat theDateFormat = new SimpleDateFormat(style);
            Instant instant = theDateFormat.parse(dateString).toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalDate();
        } catch (ParseException e) {
            throw new RuntimeException("日期转换错误", e);
        }
    }

    /**
     * 按格式输出string到date
     *
     * @param dateString
     * @param style      格式化参数
     * @return
     * @throws ParseException
     */
    public static LocalDateTime parseLocalDateTime(String dateString, String style) {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        try {
            DateFormat theDateFormat = new SimpleDateFormat(style);
            Instant instant = theDateFormat.parse(dateString).toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return instant.atZone(zoneId).toLocalDateTime();
        } catch (ParseException e) {
            throw new RuntimeException("日期转换错误", e);
        }
    }

}
