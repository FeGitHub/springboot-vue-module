package com.company.project.utils;

/****
 * 字符串工具類
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {


    /***
     *  空判斷
     * @param v
     * @return
     */
    public static boolean isEmpty(Object v) {
        if (v == null) {
            return true;
        } else {
            return v instanceof String && ((String) v).isEmpty();
        }
    }

    /***
     *  非空判斷
     * @param v
     * @return
     */
    public static boolean notEmpty(Object v) {
        return !isEmpty(v);
    }

    /***
     * 獲取對象的字符串
     * @param object
     * @return
     */
    public static String getStr(Object object) {
        return object == null ? "" : object.toString();
    }


}
