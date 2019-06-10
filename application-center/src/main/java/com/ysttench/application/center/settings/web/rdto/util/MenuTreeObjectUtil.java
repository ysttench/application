package com.ysttench.application.center.settings.web.rdto.util;

import java.lang.reflect.Field;
import java.util.Map;

public class MenuTreeObjectUtil {

    /**
     * 将Map形式的键值对中的值转换为泛型形参给出的类中的属性值 t一般代表pojo类
     * 
     * @descript
     * @param t
     * @param params
     */
    public static <T extends Object> T map2Tree(T t, Map<String, Object> params, String keyName) {
        if (params == null || t == null)
            return t;

        Class<?> clazz = t.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields = clazz.getDeclaredFields();

                for (int i = 0; i < fields.length; i++) {
                    String name = fields[i].getName(); // 获取属性的名字
                    Object value = null;
                    if ("id".equals(name) && keyName !=null && !"".equals(keyName)) {
                        value = params.get(keyName);
                    } else {
                        value = params.get(name);
                    }
                    if (value != null && !"".equals(value)) {
                        // 注意下面这句，不设置true的话，不能修改private类型变量的值
                        fields[i].setAccessible(true);
                        fields[i].set(t, value);
                    }
                }
            } catch (Exception e) {
            }

        }
        return t;
    }

}
