package com.ysttench.application.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;

public class Utils {

    /**
     * 数据库表记录主键值
     * @return
     */
    public static String getTablePK() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    /**
     * 将字符串数组转化成中间用逗号分割的字符串 "'a','b','c'"
     * @param recordIds
     * @return
     */
    public static String getRecordIds(String[] recordIds) {
        if (recordIds == null || recordIds.length == 0)
            return "";
        if (recordIds.length == 1)
            return recordIds[0];
        StringBuffer ids = new StringBuffer();
        for (int i = 0; i < recordIds.length; i++) {
            if (i == recordIds.length - 1) {
                ids.append("'" + recordIds[i] + "'");
            } else {
                ids.append("'" + recordIds[i] + "'" + ",");
            }
        }
        return ids.toString();
    }

    /**
     * 将字符串数组转化成中间用逗号分割的字符串 "'a','b','c'"
     * @param recordIds
     * @return
     */
    public static String getRecordIdsStr(String[] recordIds) {
        if (recordIds == null || recordIds.length == 0)
            return "";
        if (recordIds.length == 1)
            return "'" + recordIds[0] + "'";
        StringBuffer ids = new StringBuffer();
        for (int i = 0; i < recordIds.length; i++) {
            if (i == recordIds.length - 1) {
                ids.append("'" + recordIds[i] + "'");
            } else {
                ids.append("'" + recordIds[i] + "'" + ",");
            }
        }
        return ids.toString();
    }

    /**
     * 休息指定的时间
     * 
     * @param millisecond
     *            参数是毫秒
     */
    public static void sleep(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
        }
    }

    /**
     * 
     * @param str
     * @return
     */
    public static List<String[]> getInform(String[] str) {

        String[] recordId = new String[str.length];
        String[] email = new String[str.length];
        String[] ctype = new String[str.length];
        String[] tel = new String[str.length];
        String[] webmaster = new String[str.length];
        String[] webuser = new String[str.length];
        String[] webLogin = new String[str.length];
        int k = 0;
        for (int i = 0; i < str.length; i++) {
            java.util.StringTokenizer tokenTO = new StringTokenizer(str[i], ",");
            int j = 0;
            while (tokenTO.hasMoreTokens()) {
                try {
                    if (j == 0) {
                        recordId[k] = tokenTO.nextToken().toString();// 客户端ID号
                    } else if (j == 1) {
                        email[k] = tokenTO.nextToken().toString();// Email地址
                    } else if (j == 2) {
                        ctype[k] = tokenTO.nextToken().toString();// 网站类型
                    } else if (j == 3) {
                        tel[k] = tokenTO.nextToken().toString();// 手机号
                    } else if (j == 4) {
                        webmaster[k] = tokenTO.nextToken().toString();// 网站主名称
                    } else if (j == 5) {
                        webuser[k] = tokenTO.nextToken().toString();// 网站主用户ID
                    } else {
                        webLogin[k] = tokenTO.nextToken().toString();// 网站主登陆ID
                    }
                    j++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            k++;
        }
        List<String[]> list = new ArrayList<String[]>();
        list.add(recordId);
        list.add(email);
        list.add(ctype);
        list.add(tel);
        list.add(webmaster);
        list.add(webuser);
        list.add(webLogin);
        // map.put("recordId",(String[])recordId);
        // map.put("email",(String[])email);
        // map.put("ctype",(String[])ctype);
        // map.put("tel",(String[])tel);

        return list;
    }

    /**
     * 准装返回List
     * 
     * @param args
     */
    public static List<Map<String, String>> returnList(String name, String[] vales) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;
        for (int i = 0; i < vales.length; i++) {
            map = new HashMap<String, String>();
            map.put(name, vales[i]);
            list.add(map);
            map = null;
        }
        return list;
    }

    /**
     * 请在9:30-15:15之间执行
     * 
     * @param currDate
     *            日期
     * @return 返回boolean
     */
    public static boolean checkOperTime(Date currDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currDate);
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        if (hour < 9 || hour > 15) {
            return true;
        }
        if (hour == 9 && minute < 30) {
            return true;
        }
        if (hour == 15 && minute > 30) {
            return true;
        }
        return false;
    }

    /**
     * 出错的详细信息转化为字符串
     * 
     * @param e
     * @return 错误调用栈详情
     * 
     */
    public static String stringifyException(Throwable e) {
        StringWriter stm = new StringWriter();
        PrintWriter wrt = new PrintWriter(stm);
        e.printStackTrace(wrt);
        wrt.close();
        return stm.toString();
    }

    /**
     * 获取异常详细信息
     * 
     * @param e
     * @return
     */
    public static String getExceptionDetailInfo(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * 取的UUID生成的随机数
     * 
     * @return
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid = uuid.substring(0, uuid.indexOf("-"));
    }

    /**
     * 创建保险单号
     * 
     * @return
     */
    public static String createBXNum() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        Random r = new Random();
        return "1112151" + sdf.format(new Date()) + r.nextInt(9) + r.nextInt(9);
    }

    /**
     * 使用率计算
     * 
     * @return
     */
    public static String fromUsage(long free, long total) {
        Double d = new BigDecimal(free * 100 / total).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.valueOf(d);
    }

}
