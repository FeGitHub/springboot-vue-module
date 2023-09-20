package com.company.project.utils;

import com.company.project.vo.test.TestVo;

import java.lang.reflect.Field;
import java.util.*;

/****
 * 反射工具类
 */
public class ReflectionUtils {

    public static void main(String[] args) {
        TestVo testVo = new TestVo();
        Map<String, Field> mapField = getMapFields(TestVo.class);
        doSetFieldValue(mapField.get("testStr"), testVo, "测试一下");
        String test = getFieldValue(mapField.get("testStr"), testVo);
        System.out.println(test);

    }


    /***
     *  set方法
     * @param field
     * @param target
     * @param value
     */
    public static void doSetFieldValue(Field field, Object target, Object value) {
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception var4) {
            throw new IllegalStateException("failed to set field [" + field + "] with value : " + value, var4);
        }
    }


    /***
     *  获取
     * @param field
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T getFieldValue(Field field, Object target) {
        try {
            field.setAccessible(true);
            Object obj = field.get(target);
            return (T) obj;
        } catch (Exception var4) {
            throw new IllegalStateException("failed to get value of field: " + field, var4);
        }
    }

    /**
     * 获取类的所有字段（包括父类属性）
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
     * 获取字段映射
     * @param clazz
     * @return
     */
    public static Map<String, Field> getMapFields(Class clazz) {
        Map<String, Field> mapFields = new HashMap<>();
        Field[] fieldArr = getFields(clazz);
        if (fieldArr.length > 0) {
            for (Field field : fieldArr) {
                mapFields.put(field.getName(), field);
            }
        }
        return mapFields;
    }
}
