package com.ysttench.application.database.ibatis.util;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.common.plugin.Constant;
import com.ysttench.application.common.util.BeanUtil;
import com.ysttench.application.database.ibatis.entity.FormMap;

public class FormMapUtil {
    private static Log logger = LogFactory.getLog(FormMapUtil.class);

    /**
     * 将Map形式的键值对中的值转换为泛型形参给出的类中的属性值 t一般代表pojo类
     * 
     * @descript
     * @param t
     * @param params
     */
    public static <T extends Object> T flushObject(T t, Map<String, Object> params) {
        if (params == null || t == null)
            return t;

        Class<?> clazz = t.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields = clazz.getDeclaredFields();

                for (int i = 0; i < fields.length; i++) {
                    String name = fields[i].getName(); // 获取属性的名字
                    Object value = params.get(name);
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

    /**
     * 将一个bean结构体转成Map形式
     * @param parameterObject
     * @return
     */
    @SuppressWarnings("unchecked")
    public static FormMap<String, Object> toHashMap(Object parameterObject) {
        FormMap<String, Object> froMmap = (FormMap<String, Object>) parameterObject;
        try {
            String name = parameterObject.getClass().getName();
            Class<?> clazz = Class.forName(name);
            boolean flag = clazz.isAnnotationPresent(TableSeg.class); // 某个类是不是存在TableSeg注解
            if (flag) {
                TableSeg table = (TableSeg) clazz.getAnnotation(TableSeg.class);
                // logger.info(" 公共方法被调用,传入参数 ==>> " + froMmap);
                froMmap.put(Constant.SYS_TABLE, table.tableName());
            } else {
                throw new NullPointerException("在" + name + " 没有找到数据库表对应该的注解!");
            }
            return froMmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return froMmap;
    }

    /**
     * java对象转Map
     * 
     * @param target
     * @param map
     */
    public static <T> FormMap<String, Object> bean2FormMap(Object bean, Class<? extends FormMap<String, Object>> clazz) {
        FormMap<String, Object> map = null;
        Field[] fields = BeanUtil.getFields(bean);
        try {
            map = clazz.newInstance();
            for (Field field : fields) {
                String fieldName = field.getName();
                if (field.getModifiers() != 26) {
                    field.setAccessible(true);
                    Object value = field.get(bean);
                    if (value != null) {
                        map.put(fieldName, field.get(bean));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("java对象转Map报错");
        }

        return map;
    }

    /**
     * 将一个map结构体转成formMap形式
     * @param parameterObject
     * @return
     */
    public static FormMap<String, Object> map2FormMap(Map<String, Object> mapParam, Class<? extends FormMap<String, Object>> clazz) {
        FormMap<String, Object> froMmap = null;
        try {
            froMmap = clazz.newInstance();
            boolean flag = clazz.isAnnotationPresent(TableSeg.class); // 某个类是不是存在TableSeg注解
            if (flag) {
                TableSeg table = (TableSeg) clazz.getAnnotation(TableSeg.class);
                // logger.info(" 公共方法被调用,传入参数 ==>> " + froMmap);
                froMmap.put(Constant.SYS_TABLE, table.tableName());
            } else {
                throw new NullPointerException("在" + clazz.getName() + " 没有找到数据库表对应该的注解!");
            }
            for (String key : mapParam.keySet()) {
                froMmap.put(key, mapParam.get(key));
            }
            return froMmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return froMmap;
    }

}
