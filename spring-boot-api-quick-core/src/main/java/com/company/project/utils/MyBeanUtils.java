package com.company.project.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/****
 * bean工具类
 */
public class MyBeanUtils {


    /**
     * 大小写可以忽略
     * 下划线 _ 被忽略
     * NULL值和空字符串不会覆盖新值
     *
     * @param source
     * @param bean
     * @return
     * @throws Exception
     */
    public static <T> T copyProperties(Map<String, Object> source, Class<T> bean) throws Exception {
        Map<String, Object> sourceMap = new HashMap<String, Object>();
        source.entrySet().forEach(entry -> {
            String key = entry.getKey().toLowerCase().replace("_", "");
            sourceMap.put(key, entry.getValue());
        });
        T target = bean.getDeclaredConstructor().newInstance();
        CacheFieldMap.getFieldMap(bean).values().forEach((it) -> {
            Object field = sourceMap.get(it.getName().toLowerCase().replace("_", ""));
            if (field != null) {
                it.setAccessible(true);
                Class type = it.getType();
                try {
                    if (field != null && StringUtils.isNotEmpty(field.toString())) {
                        if(type==String.class) {
                            it.set(target, field.toString());
                        }else {
                            it.set(target, field);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        return target;
    }

    public static <T> List<T> copyProperties(List<Map<String, Object>> source, Class<T> target) {
        List<T> res = new ArrayList<T>();
        source.forEach(s -> {
            try {
                res.add(copyProperties(s, target));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return res;
    }

    private static class CacheFieldMap {
        private static Map<String, Map<String, Field>> cacheMap = new HashMap<>();

        private static Map<String, Field> getFieldMap(Class<?> clazz) {
            Map<String, Field> result = cacheMap.get(clazz.getName());
            if (result == null) {
                synchronized (CacheFieldMap.class) {
                    if (result == null) {
                        Map<String, Field> fieldMap = new HashMap<>();
                        for (Field field : clazz.getDeclaredFields()) {
                            fieldMap.put(field.getName().toLowerCase().replace("_", ""), field);
                        }
                        cacheMap.put(clazz.getName(), fieldMap);
                        result = cacheMap.get(clazz.getName());
                    }
                }
            }
            return result;
        }
    }


}
