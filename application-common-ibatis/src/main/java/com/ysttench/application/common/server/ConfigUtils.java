package com.ysttench.application.common.server;

import java.sql.Clob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.common.util.ClassUtil;
import com.ysttench.application.common.util.EhcacheUtils;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;

public class ConfigUtils {
    private static final Logger logger = Logger.getLogger(ConfigUtils.class);
    
    /**
     * 获取Mybatis的注解表和主键的关系map
     * @param packageName
     * @return
     */
    public static Map<String, Object> getTableInfo(String packageName) throws Exception {
        // 记录总记录数
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<String> classNames = ClassUtil.getClassName(packageName, true);// true包含子目录
            if (classNames != null) {
                for (String className : classNames) {
                    Class<?> clazz = Class.forName(className);
                    boolean flag = clazz.isAnnotationPresent(TableSeg.class); // 某个类是不是存在TableSeg注解
                    if (flag) {
                        TableSeg table = (TableSeg) clazz.getAnnotation(TableSeg.class);
                        map.put(table.tableName(), table.id());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("mybatis个包路径设置错误！");
        }
        return map;
    }
    
    /**
     * 初始化数据库用的参数设定
     * @param map
     * @param db
     * @return
     */
    public static Map<String, Object> getInitTableFieldParam(Map<String, Object> map, String db) {
        // 记录总记录数
        HashMap<String, Object> tm = new HashMap<String, Object>();
        
        String tabs = "";
        Set<String> keys = map.keySet();
        for(String key :keys){
            tabs += "'" + key + "',";
       }
        tabs = StringUtil.removeEnd(tabs, ',');
        // 尽量减少对数据库/IO流操作,一次查询所有表的的字段
        tm.put("table_name", tabs);
        tm.put("database_name", "'" + db + "'");

        return tm;
    }
    
    /**
     * 初始化数据库表字段到缓存
     * @param baseMapper
     * @param packageName
     * @param db
     */
    public static void initTableFieldForOracle(BaseMapper baseMapper, String packageName, String user) {
        try {
            // 记录总记录数
            Map<String, Object> map = getTableInfo(packageName);
            Map<String, Object> tm = getInitTableFieldParam(map, user);
            List<Map<String, Object>> lh = baseMapper.initTableFieldForOracle(tm);
            for (Map<String, Object> hashMap : lh) {
                Map<String, Object> m = new HashMap<String, Object>();
                Object columnName = hashMap.get("columnName");
                if (columnName instanceof Clob) {
                    m.put("field", DbUtil.oracleClob2Str((Clob)columnName));
                } else {
                    m.put("field", columnName);
                }
                String ble = hashMap.get("tableName").toString();// 表名
                m.put("column_key", map.get(ble));// 获取表的主键
                EhcacheUtils.put(ble, m);// 某表对应的主键和字段放到缓存
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" 初始化数据失败,没法加载表字段到缓存 -->> " + e.fillInStackTrace());
        }
    }
    
    /**
     * 初始化数据库表字段到缓存
     * @param baseMapper
     * @param packageName
     * @param db
     */
    public static void initTableFieldForMysql(BaseMapper baseMapper, String packageName, String db) {
        try {
            // 记录总记录数
            Map<String, Object> map = getTableInfo(packageName);
            Map<String, Object> tm = getInitTableFieldParam(map, db);
            List<Map<String, Object>> lh = baseMapper.initTableFieldForMysql(tm);
            for (Map<String, Object> hashMap : lh) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("field", hashMap.get("columnName"));
                String ble = hashMap.get("tableName").toString();// 表名
                m.put("column_key", map.get(ble));// 获取表的主键
                EhcacheUtils.put(ble, m);// 某表对应的主键和字段放到缓存
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" 初始化数据失败,没法加载表字段到缓存 -->> " + e.fillInStackTrace());
        }
    }
    
    /**
     * 初始化数据库表字段到缓存
     */
    public static void initTableFieldForMysql1(BaseMapper baseMapper, String packageName, String db) {
        // 记录总记录数
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            // List<String> classNames = getClassName(packageName);
            List<String> classNames = ClassUtil.getClassName(packageName, true);// true包含子目录
            String tabs = "";
            if (classNames != null) {
                for (String className : classNames) {
                    Class<?> clazz = Class.forName(className);
                    boolean flag = clazz.isAnnotationPresent(TableSeg.class); // 某个类是不是存在TableSeg注解
                    if (flag) {
                        TableSeg table = (TableSeg) clazz.getAnnotation(TableSeg.class);
                        tabs += "'" + table.tableName() + "',";
                        map.put(table.tableName(), table.id());
                    }
                }
            }
            tabs = tabs.substring(0, tabs.length() - 1);
            // 尽量减少对数据库/IO流操作,一次查询所有表的的字段
            HashMap<String, Object> tm = new HashMap<String, Object>();
            tm.put("table_name", tabs);
            tm.put("database_name", "'" + db + "'");
            List<HashMap<String, Object>> lh = baseMapper.initTableFieldForMysql(tm);
            for (HashMap<String, Object> hashMap : lh) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("field", hashMap.get("columnName"));
                String ble = hashMap.get("tableName").toString();// 表名
                m.put("column_key", map.get(ble));// 获取表的主键
                EhcacheUtils.put(ble, m);// 某表对应的主键和字段放到缓存
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" 初始化数据失败,没法加载表字段到缓存 -->> " + e.fillInStackTrace());
        }
    }
}
