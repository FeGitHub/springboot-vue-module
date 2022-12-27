package com.company.project.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/***
 * 字符串转换工具
 */
public class StringConvertUtil {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String STANDARD_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    public static final String STANDARD_YEAR_MONTH_FORMAT_PATTERN = "yyyy-MM";

    public static final String STANDARD_DATE_FORMAT_PATTERN_WITHCN = "yyyy年MM月dd日";

    public static final String STANDARD_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static void main(String[] args) {

    }

    /**
     * @param obj
     * @return
     */
    public static Boolean isEmptyObj(Object obj) {
        return obj == null || isEmpty(obj.toString());
    }

    /**
     * @param val
     * @return
     */
    public static boolean isEmpty(String val) {
        return StringUtils.isEmpty(val);
    }


    /***
     * 转换为 Date
     * @param dateString
     * @param style
     * @return
     */
    public static Date convertDate(String dateString, String style) {
        try {
            if (StringUtils.isEmpty(style)) {
                style = STANDARD_DATE_FORMAT_PATTERN;
            }
            DateFormat theDateFormat = new SimpleDateFormat(style);
            return theDateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new RuntimeException("日期转换错误", e);
        }
    }


    /***
     * 转成 布尔类型
     * @param val
     * @return
     */
    public static Boolean convertBoolean(String val) {
        Boolean valBoolean = null;
        if ("1".equals(val) || "true".equals(val.toLowerCase())) {
            valBoolean = true;
        } else if ("0".equals(val) || "false".equals(val.toLowerCase())) {
            valBoolean = false;
        }
        return valBoolean;
    }

    /***
     * 转成大数据
     * @param val
     * @return
     */
    public static BigDecimal convertBigDecimal(String val) {
        return isEmpty(val) ? null : new BigDecimal(val);
    }


    /***
     * 转为LocalDate
     * @param val
     * @param dateTimeFormatter
     * @return
     */
    public static LocalDate convertLocalDate(String val, DateTimeFormatter dateTimeFormatter) {
        if (dateTimeFormatter == null) {
            dateTimeFormatter = DATE_FORMATTER;
        }
        return isEmpty(val) ? null : LocalDate.parse(val, dateTimeFormatter);
    }


    /***
     * 转为 LocalTime
     * @param val
     * @return
     */
    public static LocalTime convertLocalTime(String val) {
        return isEmpty(val) ? null : LocalTime.parse(val);
    }

    /***
     * 转换为 LocalDateTime
     * @param val
     * @param style
     * @return
     */
    public static LocalDateTime convertLocalDateTime(String val, String style) {
        try {
            if (StringUtils.isEmpty(style)) {
                style = STANDARD_DATE_FORMAT_PATTERN;
            }
            DateFormat theDateFormat = new SimpleDateFormat(style);
            Instant instant = theDateFormat.parse(val).toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            return isEmpty(val) ? null : instant.atZone(zoneId).toLocalDateTime();
        } catch (ParseException e) {
            return null;
        }
    }


    /***
     * 转换为 Integer
     * @param val
     * @return
     */
    public static Integer convertInteger(String val) {
        return isEmpty(val) ? null : Integer.parseInt(val);
    }

    /***
     * 转换为 Integer
     * @param val
     * @return
     */
    public static Double convertDouble(String val) {
        return isEmpty(val) ? null : new Double(val);
    }

}
