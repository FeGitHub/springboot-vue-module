package com.company.project.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * 字符串转换工具
 */
public class StringConvertUtil {
    public static final String STANDARD_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

    public static final String STANDARD_YEAR_MONTH_FORMAT_PATTERN = "yyyy-MM";

    public static final String STANDARD_YEAR_FORMAT_PATTERN = "yyyy";

    public static final String STANDARD_DATE_FORMAT_PATTERN_WITHCN = "yyyy年MM月dd日";

    public static final String STANDARD_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";


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
     * 根据日期字符串自动获取转换的日期格式
     * @param str
     * @return
     */
    public static String convertTimeFormat(String str) {
        Pattern pattern = Pattern.compile("(\\d){4}-(\\d){2}-(\\d){2} (\\d){2}:(\\d){2}:(\\d){2}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {//yyyy-MM-dd HH:mm:ss
            return STANDARD_TIME_FORMAT_PATTERN;
        }
        pattern = Pattern.compile("(\\d){4}-(\\d){2}-(\\d){2}");
        matcher = pattern.matcher(str);
        if (matcher.find()) {//yyyy-MM-dd
            return STANDARD_DATE_FORMAT_PATTERN;
        }
        pattern = Pattern.compile("(\\d){4}-(\\d){2}");
        matcher = pattern.matcher(str);
        if (matcher.find()) {//yyyy-MM
            return STANDARD_YEAR_MONTH_FORMAT_PATTERN;
        }
        pattern = Pattern.compile("(\\d){4}年(\\d){2}月(\\d){2}日");
        matcher = pattern.matcher(str);
        if (matcher.find()) {//yyyy年MM月dd日
            return STANDARD_DATE_FORMAT_PATTERN_WITHCN;
        }
        pattern = Pattern.compile("(\\d){4}");
        matcher = pattern.matcher(str);
        if (matcher.find()) {//yyyy
            return STANDARD_YEAR_FORMAT_PATTERN;
        }
        return "";
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
                style = convertTimeFormat(dateString);
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
     * @param style
     * @return
     */
    public static LocalDate convertLocalDate(String val, String style) {
        if (isEmpty(style)) {
            style = convertTimeFormat(val);
        }
        if (!isEmpty(val)) {
            try {
                DateFormat theDateFormat = new SimpleDateFormat(style);
                Instant instant = theDateFormat.parse(val).toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                return instant.atZone(zoneId).toLocalDate();
            } catch (ParseException e) {
                throw new RuntimeException("日期轉換錯誤", e);
            }
        }
        return null;
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
                style = convertTimeFormat(val);
            }
            if (!isEmpty(val)) {
                DateFormat theDateFormat = new SimpleDateFormat(style);
                Instant instant = theDateFormat.parse(val).toInstant();
                ZoneId zoneId = ZoneId.systemDefault();
                return instant.atZone(zoneId).toLocalDateTime();
            }
            return null;
        } catch (ParseException e) {
            throw new RuntimeException("日期转换错误", e);
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
     * 转换为 Double
     * @param val
     * @return
     */
    public static Double convertDouble(String val) {
        return isEmpty(val) ? null : Double.parseDouble(val);
    }


    /***
     * 转换为 Long
     * @param val
     * @return
     */
    public static Long convertLong(String val) {
        return isEmpty(val) ? null : Long.parseLong(val);
    }


    /***
     * 转换为 Byte
     * @param val
     * @return
     */
    public static Byte convertByte(String val) {
        return isEmpty(val) ? null : Byte.parseByte(val);
    }

    /***
     * 转换为 Short
     * @param val
     * @return
     */
    public static Short convertShort(String val) {
        return isEmpty(val) ? null : Short.parseShort(val);
    }

    /***
     * 转换为 Char
     * @param val
     * @return
     */
    public static Character convertCharacter(String val) {
        return isEmpty(val) ? null : val.charAt(0);
    }

    /***
     * 字符串转list
     * @param str
     * @param splitCode
     * @return
     */
    public static List<String> splitStrToList(String str, String splitCode) {
        List<String> result = new ArrayList<>();
        splitCode = isEmpty(splitCode) ? "," : splitCode;
        if (!isEmpty(str)) {
            if (str.indexOf(splitCode) > -1) {
                result = Arrays.asList(str.split(splitCode));
            } else {
                result.add(str);
            }
        }
        return result;
    }


    public static void main(String[] args) {
        LocalDate testLocalDate = convertLocalDate("2025-02-08", null);
        LocalDate testLocalDate2 = convertLocalDate("2025-03", null);
        if (testLocalDate2.isAfter(testLocalDate)) {
            System.out.println(testLocalDate2.toString() + "大");
        } else {
            System.out.println(testLocalDate.toString() + "大");
        }
    }
}
