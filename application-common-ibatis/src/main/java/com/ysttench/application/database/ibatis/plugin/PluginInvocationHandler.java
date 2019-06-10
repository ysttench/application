/*
 *    Copyright 2009-2012 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.ysttench.application.database.ibatis.plugin;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.PluginException;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.ExceptionUtil;

import com.ysttench.application.common.plugin.Constant;
import com.ysttench.application.common.plugin.PagePlugin;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.EhcacheUtils;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.database.ibatis.util.FormMapUtil;

/**
 * @author Clinton Begin
 */
public class PluginInvocationHandler implements InvocationHandler {

    private Object target;
    private Interceptor interceptor;
    private Map<Class<?>, Set<Method>> signatureMap;

    private PluginInvocationHandler(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
        this.target = target;
        this.interceptor = interceptor;
        this.signatureMap = signatureMap;
    }

    public static Object wrap(Object target, Interceptor interceptor) {
        Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
        Class<?> type = target.getClass();
        Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
        if (interfaces.length > 0) {
            return Proxy.newProxyInstance(type.getClassLoader(), interfaces, new PluginInvocationHandler(target, interceptor,
                    signatureMap));
        }
        return target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Set<Method> methods = signatureMap.get(method.getDeclaringClass());
            if (methods != null && methods.contains(method)) {
                return interceptor.intercept(new Invocation(target, method, args));
            }
            return method.invoke(target, args);
        } catch (Exception e) {
            throw ExceptionUtil.unwrapThrowable(e);
        }
    }

    private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
        Intercepts interceptsAnnotation = interceptor.getClass().getAnnotation(Intercepts.class);
        if (interceptsAnnotation == null) { // issue #251
            throw new PluginException("No @Intercepts annotation was found in interceptor "
                    + interceptor.getClass().getName());
        }
        Signature[] sigs = interceptsAnnotation.value();
        Map<Class<?>, Set<Method>> signatureMap = new HashMap<Class<?>, Set<Method>>();
        for (Signature sig : sigs) {
            Set<Method> methods = signatureMap.get(sig.type());
            if (methods == null) {
                methods = new HashSet<Method>();
                signatureMap.put(sig.type(), methods);
            }
            try {
                Method method = sig.type().getMethod(sig.method(), sig.args());
                methods.add(method);
            } catch (NoSuchMethodException e) {
                throw new PluginException("Could not find method on " + sig.type() + " named " + sig.method()
                        + ". Cause: " + e, e);
            }
        }
        return signatureMap;
    }

    private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        while (type != null) {
            for (Class<?> c : type.getInterfaces()) {
                if (signatureMap.containsKey(c)) {
                    interfaces.add(c);
                }
            }
            type = type.getSuperclass();
        }
        return interfaces.toArray(new Class<?>[interfaces.size()]);
    }

    @SuppressWarnings("unchecked")
    public static final String joinSql(Connection connection, MappedStatement mappedStatement,
            BoundSql boundSql, Map<String, Object> formMap, List<Object> formMaps) throws Exception {
        Object table = null;
        String sql = "";
        Map<String, Object> mapfield = null;
        String field = null;
        if (null != formMap) {
            table = formMap.get(Constant.SYS_TABLE);
            mapfield = (Map<String, Object>) EhcacheUtils.get(table);
            field = mapfield.get("field").toString();
            sql = " select " + field + " from " + String.valueOf(table);
        }
        String sqlId = mappedStatement.getId();
        sqlId = sqlId.substring(sqlId.lastIndexOf(".") + 1);
        if (Constant.FINDBYWHERE.equals(sqlId)) {
            sql += getWhereSqlByWhere(formMap);
            sql += getOrderBySql(formMap);
        } else if (Constant.FINDBYPAGE.equals(sqlId)) {
            sql += getWhereSqlByNames(formMap, field);
            sql += getOrderBySql(formMap);
            Object paging = formMap.get("paging");
            if (null == paging) {
                throw new Exception("调用findByPage接口,必须传入PageView对象! formMap.set(\"paging\", pageView);");
            } else if (StringUtils.isBlank(paging.toString())) {
                throw new Exception("调用findByPage接口,必须传入PageView对象! formMap.set(\"paging\", pageView);");
            }
            PageView pageView = (PageView) paging;
            setCount(sql, connection, boundSql, pageView);
            sql = PagePlugin.generatePagesSql(sql, pageView);
        } else if (Constant.DELETEBYNAMES.equals(sqlId)) {
            sql = "delete from " + table.toString();
            sql += getWhereSqlByNames(formMap, field);
        } else if (Constant.DELETEBYATTRIBUTE.equals(sqlId)) {
            sql = "delete from " + table.toString();
            sql += getWhereSqlByAttr(formMap);
        } else if (Constant.FINDBYNAMES.equals(sqlId)) {
            sql += getWhereSqlByNames(formMap, field);
            sql += getOrderBySql(formMap);
        } else if (Constant.FINDBYATTRIBUTE.equals(sqlId)) {
            sql = "select * from " + table.toString() ;
            sql += getWhereSqlByAttr(formMap);
            sql += getOrderBySql(formMap);
        } else if (Constant.ADDENTITY.equals(sqlId)) {
            String[] fe = field.split(",");
            String fieldString = "";
            String fieldValues = "";
            for (String string : fe) {
                Object v = formMap.get(StringUtil.tablefield2prop(string));
                if (null != v && !StringUtils.isBlank(v.toString())) {
                    fieldString += string + ",";
                    fieldValues += getFieldSql(v) + ",";
                }
            }
            sql = "insert into " + table.toString() + " (" + StringUtil.removeEnd(fieldString)
                    + ")  values (" + StringUtil.removeEnd(fieldValues) + ")";
        } else if (Constant.EDITENTITY.equals(sqlId)) {
            String[] fe = field.split(",");
            String fieldString = "";
            String where = "";
            for (String string : fe) {
                if (formMap.containsKey(StringUtil.tablefield2prop(string))) {
                    Object v = formMap.get(StringUtil.tablefield2prop(string));
                    String key = mapfield.get(Constant.COLUMN_KEY).toString();
                    if (!StringUtils.isBlank(key)) {
                        if (key.equals(string)) {
                            where = "WHERE " + key + " = " + getFieldSql(v);
                        } else {
                            fieldString = getEditFieldSql(fieldString, string, v);
                        }
                    } else {
                        throw new NullPointerException("update操作没有找到主键!");
                    }

                }
            }

            sql = "update " + table.toString() + " set " + StringUtil.removeEnd(fieldString) + " " + where;
        } else if (Constant.FINDBYFRIST.equals(sqlId)) {
            sql = "select * from " + table.toString() + " where " + StringUtil.prop2tablefield(formMap.get("key").toString());
            if (null != formMap.get("value") && !"".equals(formMap.get("value").toString())) {
                sql += " = '" + formMap.get("value") + "'";
            } else {
                throw new Exception(sqlId + " 调用公共方法异常!,传入参数错误！");
            }

        } else if (Constant.BATCHSAVE.equals(sqlId)) {
            if (null != formMaps && formMaps.size() > 0) {
                table = FormMapUtil.toHashMap(formMaps.get(0)).get(Constant.SYS_TABLE);
                mapfield = (Map<String, Object>) EhcacheUtils.get(table);
                field = mapfield.get(Constant.FIELD).toString();
            }
            sql = generateBatchSaveSql(formMaps, table, field);

        } else {
            throw new Exception("调用公共方法异常!");
        }
        return sql;
    }

    /**
     * 获取编辑字段
     * @param fieldString
     * @param string
     * @param v
     * @return
     */
    private static String getFieldSql(Object v) {
        String fieldSql = "";
        if (v instanceof Integer || v instanceof Long || v instanceof Double || v instanceof Float || v instanceof BigDecimal) {
            fieldSql = v.toString();
        } else if (v instanceof Date && PagePlugin.getDialect().equals("oracle")) {
            fieldSql = "to_date('" + DatetimeUtil.formatDate((Date)v, DatetimeUtil.DATE_TIME_PATTERN) + "','" + DatetimeUtil.ORACLE_DATE_TIME_PATTERN + "')";
        } else if (PagePlugin.getDialect().equals("oracle") && "sysdate".equals(v.toString())) {
            fieldSql = v.toString();
        } else {
            fieldSql = "'" + v + "'";
        }
        return fieldSql;
    }

    /**
     * 获取编辑字段
     * @param fieldString
     * @param string
     * @param v
     * @return
     */
    private static String getEditFieldSql(String fieldString, String string, Object v) {
        if (v == null) {
            fieldString += string + "= null,";
        } else if (v instanceof Integer || v instanceof Long || v instanceof Double || v instanceof Float || v instanceof BigDecimal) {
            fieldString += string + "=" + v + ",";
        } else if (v instanceof Date && PagePlugin.getDialect().equals("oracle")) {
            fieldString += string + "=to_date('" + DatetimeUtil.formatDate((Date)v, DatetimeUtil.DATE_TIME_PATTERN) + "','" + DatetimeUtil.ORACLE_DATE_TIME_PATTERN + "'),";
        } else if (PagePlugin.getDialect().equals("oracle") && "sysdate".equals(v.toString())) {
            fieldString += string + "=" + v + ",";
        } else {
            fieldString += string + "='" + v + "',";
        }
        return fieldString;
    }

    /**
     * 根据参数获取查询条件sql文
     * @param formMap
     * @return
     */
    private static String getWhereSqlByAttr(Map<String, Object> formMap) {
        String sql = " where " + StringUtil.prop2tablefield(formMap.get("key").toString());
        Object v = formMap.get("value");
        if (null != v && v.toString().indexOf("%") > -1)// 处理模糊查询
        {
            sql += " LIKE '" + v + "'";
        } else {
            sql += " in (" + getFieldSql(v) + ")";
        }
        return sql;
    }

    /**
     * 根据Names获取查询条件sql文
     * @param formMap
     * @param sql
     * @param field
     * @return
     */
    private static String getWhereSqlByNames(Map<String, Object> formMap, String field) {
        String sql = "";
        String[] fe = field.split(",");
        String param = "";
        for (String string : fe) {
            if (formMap.containsKey(StringUtil.tablefield2prop(string))) {
                Object v = formMap.get(StringUtil.tablefield2prop(string));
                if (v instanceof List || v.getClass().isArray()) {
                    param += " and " + string + " in " + doConditionArray(v);
                 // 处理模糊查询
                } else if (v.toString().indexOf("%") > -1) {
                    param += " and " + string + " like '" + v + "'";
                } else {
                    param += " and " + string + " = " + getFieldSql(v);
                }
            }
        }
        if (StringUtils.isNotBlank(param)) {
            param = param.substring(param.indexOf("and") + 4);
        } else {
            param = " 1=1 ";
        }
        sql += " where " + param + getWhereSqlByWhere(formMap);
        
        return sql;
    }

    /**
     * 获取排序信息
     * @param formMap
     * @param sql
     * @return
     */
    private static String getOrderBySql(Map<String, Object> formMap) {
        String sql = "";
        Object by = formMap.get("orderby");
        if (null != by) {
            sql += " " + by;
        }
        return sql;
    }

    /**
     * 通过where条件获取查询条件sql文
     * @param formMap
     * @param sql
     * @return
     */
    private static String getWhereSqlByWhere(Map<String, Object> formMap) {
        String sql = "";
        if (null != formMap.get("where") && !StringUtils.isBlank(formMap.get("where").toString())) {
            sql += " " + formMap.get("where").toString();
        }
        return sql;
    }

    /**
     * 构建批量创建sql语句
     * @param formMaps
     * @param table
     * @param field
     * @return
     */
    private static String generateBatchSaveSql(List<Object> formMaps, Object table, String field) {
        if (PagePlugin.getDialect().equals("mysql")) {
            return generateMysqlBatchSaveSql(formMaps, table, field);
        } else if (PagePlugin.getDialect().equals("oracle")) {
            return generateOracleBatchSaveSql(formMaps, table, field);
        }
        return "";
    }

    /**
     * 构建mysql的批量更新sql
     * @param formMaps
     * @param table
     * @param field
     * @return
     */
    @SuppressWarnings("unchecked")
    private static String generateMysqlBatchSaveSql(List<Object> formMaps, Object table, String field) {
        String sql;
        sql = "";
        String fieldString = "";
        String fs = "";
        String fd = "";
        String fieldValues = "";
        String fvs = "";
        for (int i = 0; i < formMaps.size(); i++) {
            Object object = formMaps.get(i);
            Map<String, Object> froMmap = (Map<String, Object>) object;
            String[] fe = field.split(",");
            for (String string : fe) {
                Object v = froMmap.get(StringUtil.tablefield2prop(string));
                if (null != v && !StringUtils.isBlank(v.toString())) {
                    fieldString += string + ",";
                    fieldValues += getFieldSql(v) + ",";
                }
            }
            if (i == 0) {
                fd = fieldString;
            }
            fvs += "(" + StringUtil.removeEnd(fieldValues) + "),";
            fs += " insert into " + table.toString() + " (" + StringUtil.removeEnd(fieldString)
                    + ")  values (" + StringUtil.removeEnd(fieldValues) + ");";
            fieldValues = "";
            fieldString = "";
        }
        String v = StringUtil.removeEnd(fvs);
        sql = "insert into " + table.toString() + " (" + StringUtil.removeEnd(fd) + ")  values "
                + StringUtil.removeEnd(fvs) + "";
        String[] vs = v.split("\\),");
        boolean b = false;
        for (String string : vs) {
            if (string.split(",").length != fd.split(",").length) {
                b = true;
            }
        }
        if (b) {
            sql = fs.substring(0, fs.length() - 1);
        }
        return sql;
    }

    /**
     * 构建批量创建Oracle语句
     * @param formMaps
     * @param table
     * @param field
     * @return
     */
    @SuppressWarnings("unchecked")
    private static String generateOracleBatchSaveSql(List<Object> formMaps, Object table, String field) {
        String sql;
        sql = "insert all ";
        String fieldString = "";
        String fs = "";
        String fieldValues = "";
        for (int i = 0; i < formMaps.size(); i++) {
            Object object = formMaps.get(i);
            Map<String, Object> froMmap = (Map<String, Object>) object;
            String[] fe = field.split(",");
            for (String string : fe) {
                Object v = froMmap.get(StringUtil.tablefield2prop(string));
                if (null != v && !StringUtils.isBlank(v.toString())) {
                    fieldString += string + ",";
                    fieldValues += getFieldSql(v) + ",";
                }
            }
            fs += " into " + table.toString() + " (" + StringUtil.removeEnd(fieldString)
                    + ")  values (" + StringUtil.removeEnd(fieldValues) + ") ";
            fieldValues = "";
            fieldString = "";
        }
        sql += fs.substring(0, fs.length() - 1) + " SELECT 1 FROM DUAL";
        return sql;
    }

    /**
     * 获取指定数组类型条件sql文
     * @param v
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static String doConditionArray(Object v) {
        String strParam = "";
        if (v instanceof List) {
            strParam += "(";
            for (Object aparam : (List)v) {
                strParam = contactColumn(strParam, aparam);
            }
            strParam = StringUtil.removeEnd(strParam, ',');
            strParam += ")";
        } else if (v.getClass().isArray()) {
            strParam += "(";
            for (Object aparam : (Object[])v) {
                strParam = contactColumn(strParam, aparam);
            }
            strParam = StringUtil.removeEnd(strParam, ',');
            strParam += ")";
        }
        return strParam;
    }

    /**
     * 条件字符串拼接
     * @param strParam
     * @param aparam
     * @return
     */
    private static String contactColumn(String strParam, Object aparam) {
        if (aparam instanceof Integer || aparam instanceof Long || aparam instanceof Double || aparam instanceof Float || aparam instanceof BigDecimal) {
            strParam += aparam + ",";
        } else {
            strParam += "'" + aparam + "',";
        }
        return strParam;
    }

    /**
     * 设定分页总数
     * @param sql
     * @param connection
     * @param boundSql
     * @param pageView
     * @throws SQLException
     */
    @SuppressWarnings("resource")
    public static void setCount(String sql, Connection connection, BoundSql boundSql, PageView pageView)
            throws SQLException {
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            String countSql = "";
            try {
                countSql = "select count(1) from " + suffixStr(removeOrderBys(sql));
                countStmt = connection.prepareStatement(countSql);
                rs = countStmt.executeQuery();
            } catch (Exception e) {
                PagePlugin.logger.error(countSql + " 统计Sql出错,自动转换为普通统计Sql语句!");
                countSql = "select count(1) from (" + sql + ") tmp_count";
                countStmt = connection.prepareStatement(countSql);
                rs = countStmt.executeQuery();
            }
            int count = 0;
            if (rs.next()) {
                count = ((Number) rs.getObject(1)).intValue();
            }
            pageView.setRowCount(count);
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                countStmt.close();
            } catch (Exception e) {
            }
        }

    }

    public static String suffixStr(String toSql) {
        int sun = toSql.indexOf("from");
        String f1 = toSql.substring(sun - 1, sun);
        String f2 = toSql.substring(sun + 4, sun + 5);
        if (f1.trim().isEmpty() && f2.trim().isEmpty()) {
            String s1 = toSql.substring(0, sun);
            int s0 = s1.indexOf("(");
            if (s0 > -1) {
                int se1 = s1.indexOf("select");
                if (s0 < se1) {
                    if (se1 > -1) {
                        String ss1 = s1.substring(se1 - 1, se1);
                        String ss2 = s1.substring(se1 + 6, se1 + 7);
                        if (ss1.trim().isEmpty() && ss2.trim().isEmpty()) {
                            return suffixStr(toSql.substring(sun + 5));
                        }
                    }
                }
                int se2 = s1.indexOf("(select");
                if (se2 > -1) {
                    String ss2 = s1.substring(se2 + 7, se2 + 8);
                    if (ss2.trim().isEmpty()) {
                        return suffixStr(toSql.substring(sun + 5));
                    }
                }
                if (se1 == -1 && se2 == -1) {
                    return toSql.substring(sun + 5);
                } else {
                    toSql = toSql.substring(sun + 5);
                }
            } else {
                toSql = toSql.substring(sun + 5);
            }
        }
        return toSql;
    }

    private static String removeOrderBys(String toSql) {
        int sun = toSql.indexOf("order");
        if (sun > -1) {
            String f1 = toSql.substring(sun - 1, sun);
            String f2 = toSql.substring(sun + 5, sun + 5);
            if (f1.trim().isEmpty() && f2.trim().isEmpty()) {
                String zb = toSql.substring(sun);
                int s0 = zb.indexOf(")");
                if (s0 > -1) {
                    String s1 = toSql.substring(0, sun);
                    String s2 = zb.substring(s0);
                    return removeOrderBys(s1 + s2);
                } else {
                    toSql = toSql.substring(0, sun);
                }
            }
        }
        return toSql;
    }
}
