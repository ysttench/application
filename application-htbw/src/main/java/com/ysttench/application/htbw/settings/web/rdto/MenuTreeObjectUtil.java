package com.ysttench.application.htbw.settings.web.rdto;

import java.lang.reflect.Field;
import java.util.Map;

public class MenuTreeObjectUtil {

    /**
     * ��Map��ʽ�ļ�ֵ���е�ֵת��Ϊ�����βθ��������е�����ֵ tһ�����pojo��
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
                    String name = fields[i].getName(); // ��ȡ���Ե�����
                    Object value = null;
                    if ("id".equals(name) && keyName !=null && !"".equals(keyName)) {
                        value = params.get(keyName);
                    } else {
                        value = params.get(name);
                    }
                    if (value != null && !"".equals(value)) {
                        // ע��������䣬������true�Ļ��������޸�private���ͱ�����ֵ
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
