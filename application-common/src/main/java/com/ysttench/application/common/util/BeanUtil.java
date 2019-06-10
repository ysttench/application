package com.ysttench.application.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 实体对象工具类
 * 
 * @author 潘益孟
 * 
 */
public class BeanUtil {
    private static Log logger = LogFactory.getLog(BeanUtil.class);

    /**
     * 获取对象属性值
     * 
     * @param target
     * @param fieldName
     * @return
     */
    public static <T> Object getPropertyValue(T target, String fieldName) {
        try {
            return PropertyUtils.getSimpleProperty(target, fieldName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("读取指定对象字段值出错：" + target.getClass().getName() + "." + fieldName);
        }
        return new Object();
    }

    /**
     * 获取对象类型
     * 
     * @param target
     * @param fieldName
     * @return
     */
    public static <T> Class<?> getPropertyType(T target, String fieldName) {
        try {
            return PropertyUtils.getPropertyType(target, fieldName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("读取指定对象字段类型出错：" + target.getClass().getName() + "." + fieldName);
        }
        return null;
    }

    /**
     * 设置对象属性值
     * 
     * @param target
     * @param fieldName
     * @param value
     */
    public static <T> void setPropertyValue(T target, String fieldName, Object value) {
        try {
//            Class<?> classType = getPropertyType(target, fieldName);
//            if (value instanceof BLOB) {
//                value = ((BLOB) value).getBytes();
//            } else if (value instanceof CLOB) {
//                value = ((CLOB) value).getBytes();
//            } else {
//                value = classType == null ? null : TypeUtils.castToJavaBean(value, classType);
//                // value = TypeUtils.castToJavaBean(value, classType);
//            }
            if (hasProperty(target, fieldName)) {
                PropertyUtils.setProperty(target, fieldName, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("读取指定对象字段类型出错：" + target.getClass().getName() + "." + fieldName);
        }
    }

    /**
     * 获取对象字段Field
     * 
     * @param target
     * @param fieldName
     * @return
     */
    public static <T> Field getField(T target, String fieldName) {
        try {
            return target.getClass().getDeclaredField(fieldName);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("读取指定对象字段类型出错：" + target.getClass().getName() + "." + fieldName);
        }
        return null;
    }

    /**
     * 获取java对象区域
     * 
     * @param target
     * @param fieldName
     * @return
     */
    public static <T> Field[] getFields(T target) {
        Field[] fields = target.getClass().getDeclaredFields();
        return fields;
    }

    /**
     * java对象转Map
     * 
     * @param target
     * @param map
     */
    public static <T> Map<String, Object> bean2Map(T target) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = getFields(target);
        for (Field field : fields) {
            String fieldName = field.getName();
            try {
                if (field.getModifiers() != 26) {
                    field.setAccessible(true);
                    Object value = field.get(target);
                    if (value != null) {
                        map.put(fieldName, field.get(target));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("java对象转Map报错");
            }
        }

        return map;
    }

    /**
     * Map对象转java
     * 
     * @param target
     * @param map
     */
    public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz) {
        T target = null;
        try {
            if (map != null) {
                target = clazz.newInstance();
                map2Bean(map, target);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Map对象转java报错");
        }
        return target;
    }

    /**
     * Map对象转java
     * 
     * @param target
     * @param map
     */
    public static <T> void map2Bean(Map<String, Object> map, T target) {
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object value = map.get(key);
            if (value == null)
                continue;
            String fieldName = getFieldName(key);
            try {
                PropertyDescriptor des = PropertyUtils.getPropertyDescriptor(target, fieldName);
                if (des == null) {
                    throw new Exception();
                }
                setPropertyValue(target, fieldName, value);
            } catch (Exception e) {
                PropertyDescriptor[] descs = PropertyUtils.getPropertyDescriptors(target);
                for (PropertyDescriptor desc : descs) {
                    String columnName = desc.getDisplayName();
                    if (columnName.toLowerCase().equals(fieldName)) {
                        setPropertyValue(target, columnName, value);
                        break;
                    }
                }
            }
        }
    }

    /**
     * ListMap对象转java
     * 
     * @param target
     * @param map
     */
    public static <T> List<T> maps2Bean(List<Map<String, Object>> maps, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        for (Map<String, Object> map : maps) {
            T target = null;
            try {
                target = clazz.newInstance();
                map2Bean(map, target);
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("ListMap对象转java报错");
            }
            list.add(target);
        }
        return list;
    }

    /**
     * 是否有属性
     * 
     * @param target
     * @param fieldName
     * @return
     */
    public static <T> boolean hasProperty(T target, String fieldName) {
        boolean check = true;
        try {
            PropertyDescriptor des = PropertyUtils.getPropertyDescriptor(target, fieldName);
            if (des.getWriteMethod() == null) {
                check = false;
            }
        } catch (Exception e) {
            check = false;
        }
        return check;
    }

    /**
     * 获取字段名称
     * 
     * @param columnName
     * @return
     */
    public static <T> String getFieldName(String columnName) {
        String csp[] = columnName.split("_");
        if (csp.length == 0) {
            return columnName;
        } else {
            StringBuilder propertyName = new StringBuilder();
            for (int i = 0; i < csp.length; i++) {
                String ns = csp[i];
                if (i == 0) {
                    propertyName.append(ns.toLowerCase());
                } else {
                    propertyName.append(ns.substring(0, 1).toUpperCase() + ns.substring(1).toLowerCase());
                }
            }

            return propertyName.toString();
        }
    }

}
