package com.company.project.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


/***
 * map轉bean
 */
public class MapToBeanUtils {


    public static void main(String[] args) {

    }

    /***
     * 配置信息vo
     */
    public static class MapToBeanConfig {

        /***
         * 設置無需複製的字段
         * @return
         */
        public Set<String> getIgnoreFiled() {
            return ignoreFiled;
        }

        public void setIgnoreFiled(Set<String> ignoreFiled) {
            this.ignoreFiled = ignoreFiled;
        }

        public List<String> getPackagePathList() {
            return packagePathList;
        }

        public void setPackagePathList(List<String> packagePathList) {
            this.packagePathList = packagePathList;
        }

        public Set<String> ignoreFiled = null;//无需映射处理的实体字段
        public List<String> packagePathList = null;
    }


    /***
     * map转bean
     * @param map
     * @param t
     * @param mapToBeanConfig
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T t, MapToBeanConfig mapToBeanConfig) {
        Set<String> ignoreFiled = mapToBeanConfig.getIgnoreFiled();
        List<String> packagePathList = mapToBeanConfig.getPackagePathList();
        Field[] field = getFields(t.getClass());
        String name = "";
        String type = "";
        String packagePath = "";
        for (int j = 0; j < field.length; j++) { //遍历所有属性
            name = field[j].getName(); //获取属性的名字
            type = field[j].getGenericType().toString(); //获取属性的类型
            packagePath = field[j].getDeclaringClass().getName();
            Boolean isMapHaveKey = map.containsKey(name);//對象的map是否有屬性值對應的key
            try {
                if (ignoreFiled == null || (ignoreFiled != null && !ignoreFiled.contains(name))) {
                    if (!strLikeListStr(packagePathList, packagePath)) {
                        setMethod(type, name, t, map.get(name), isMapHaveKey, mapToBeanConfig);
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return t;
    }


    /***
     * 字符串是否被list的字符串所包含
     * @param list
     * @param str
     * @return
     */
    public static boolean strLikeListStr(List<String> list, String str) {
        if (CollectionUtils.isNotEmpty(list)) {
            String likeName;
            for (int i = 0; i < list.size(); i++) {
                likeName = list.get(i) != null ? list.get(i) : "";
                if (str.indexOf(likeName) > -1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取所有字段
     *
     * @param clazz
     * @return
     */
    public static Field[] getFields(Class clazz) {
        List<Field> fieldList = new ArrayList<>(16);
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fields));
            clazz = clazz.getSuperclass();
        }
        Field[] f = new Field[fieldList.size()];
        return fieldList.toArray(f);
    }


    /***
     * 值複製
     * @param type
     * @param name
     * @param t
     * @param val
     * @param <T>
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private static <T> void setMethod(String type, String name, T t, Object val, Boolean isMapHaveKey, MapToBeanConfig mapToBeanConfig) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Boolean isOtherType = false;
        Method m = null;
        if (type.equals("class java.lang.String")) {
            m = t.getClass().getMethod(setMethod(name), new Class[]{String.class});
            if (!StringConvertUtil.isEmptyObj(val)) {
                m.invoke(t, new Object[]{val.toString()});
            }
        } else if (type.equals("class java.lang.Boolean")) {
            m = t.getClass().getMethod(setMethod(name), new Class[]{Boolean.class});
            if (!StringConvertUtil.isEmptyObj(val)) {
                m.invoke(t, new Object[]{StringConvertUtil.convertBoolean(val.toString())});
            }
        } else if (type.equals("class java.time.LocalDate")) {
            m = t.getClass().getMethod(setMethod(name), new Class[]{LocalDate.class});
            if (!StringConvertUtil.isEmptyObj(val)) {
                m.invoke(t, new Object[]{StringConvertUtil.convertLocalDate(val.toString(), null)});
            }
        } else if (type.equals("class java.math.BigDecimal")) {
            m = t.getClass().getMethod(setMethod(name), new Class[]{BigDecimal.class});
            if (!StringConvertUtil.isEmptyObj(val)) {
                m.invoke(t, new Object[]{StringConvertUtil.convertBigDecimal(val.toString())});
            }
        } else if (type.equals("class java.time.LocalDateTime")) {
            m = t.getClass().getMethod(setMethod(name), new Class[]{LocalDateTime.class});
            if (!StringConvertUtil.isEmptyObj(val)) {
                m.invoke(t, new Object[]{StringConvertUtil.convertLocalDateTime(val.toString(), null)});
            }
        } else if (type.equals("class java.time.LocalTime")) {
            m = t.getClass().getMethod(setMethod(name), new Class[]{LocalTime.class});
            if (!StringConvertUtil.isEmptyObj(val)) {
                m.invoke(t, new Object[]{StringConvertUtil.convertLocalTime(val.toString())});
            }
        } else if (type.equals("class java.util.Date")) {
            m = t.getClass().getMethod(setMethod(name), new Class[]{Date.class});
            if (!StringConvertUtil.isEmptyObj(val)) {
                m.invoke(t, new Object[]{StringConvertUtil.convertDate(val.toString(), null)});
            }
        } else if (isBasicDataType(type)) {//基本数据类型的转换
            setMethodBasicDataType(m, type, t, name, val);
        } else {
            //其他的不做轉換，自行去處理
            isOtherType = true;
            //  System.out.println("要獨立轉換的字段：" + name + ", 对应的类型为:" + type);
        }
        // 如果已知數據類型，並且map是有值的，但是為空，此時認定置空
        if (!isOtherType && isMapHaveKey && StringConvertUtil.isEmptyObj(val) && m != null) {
            m.invoke(t, new Object[]{null});
        }
    }


    /**
     * 是否是基本數據類型
     *
     * @param type
     * @return
     */
    public static boolean isBasicDataType(String type) {
        return type.equals("class java.lang.Integer") || type.equals("int")
                || type.equals("class java.lang.Double") || type.equals("double");
    }


    /***
     * 基本數據類型的轉換
     * @param m
     * @param type
     * @param t
     * @param name
     * @param val
     * @param <T>
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> void setMethodBasicDataType(Method m, String type, T t, String name, Object val) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (type.equals("class java.lang.Integer")) {
            m = t.getClass().getMethod(setMethod(name), new Class[]{Integer.class});
            if (!StringConvertUtil.isEmptyObj(val)) {
                m.invoke(t, new Object[]{StringConvertUtil.convertInteger(val.toString())});
            }
        } else if (type.equals("int")) {
            m = t.getClass().getMethod(setMethod(name), new Class[]{int.class});
            if (!StringConvertUtil.isEmptyObj(val)) {
                m.invoke(t, new Object[]{StringConvertUtil.convertInteger(val.toString())});
            }
        } else if (type.equals("class java.lang.Double")) {
            m = t.getClass().getMethod(setMethod(name), new Class[]{Double.class});
            if (!StringConvertUtil.isEmptyObj(val)) {
                m.invoke(t, new Object[]{StringConvertUtil.convertDouble(val.toString())});
            }
        } else if (type.equals("double")) {
            m = t.getClass().getMethod(setMethod(name), new Class[]{double.class});
            if (!StringConvertUtil.isEmptyObj(val)) {
                m.invoke(t, new Object[]{StringConvertUtil.convertDouble(val.toString())});
            }
        }
    }

    /***
     * 对应属性字段的的set方法
     * @param fildeName
     * @return
     */
    private static String setMethod(String fildeName) {
        return "set" + firstLetterToUpperCase(fildeName);
    }


    /***
     * 首字母变成大写
     * @param str
     * @return
     */
    private static String firstLetterToUpperCase(String str) {
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }


}



