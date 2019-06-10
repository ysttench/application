package com.ysttench.application.common.util;

import org.apache.commons.lang.StringUtils;

public class ArrayUtils {

    public static boolean arrayIsEmpty(String[] strs) {
        if (strs == null || strs.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 使得数组不为空
     * 
     * @param data
     * @return
     */
    public static String[] formatArray(String[] data) {
        if (data == null) {
            return new String[0];
        }
        return data;
    }

    /**
     * 比较数组中的数据是否有相等的数据
     * 
     * @param str
     * @return
     */
    public static boolean isEquals(String[] inptstr, String param) {

        if (null == inptstr)
            return false;
        if (StringUtils.isBlank(param) || param.equals("http://"))
            param = "";
        for (int i = 0; i < inptstr.length; i++) {
            for (int j = 0; j < inptstr.length; j++) {
                if ((inptstr[i].equals(inptstr[j]) && i != j) || (inptstr[j].toLowerCase()).equals(param.trim())
                        || StringUtils.isBlank(inptstr[i]) || inptstr[i].equals("http://")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将字符串数组转化成中间用逗号分割的字符串 "a,b,c"
     * @param strs
     * @return
     */
    public static String getStrs(String[] strs) {
        if (strs == null || strs.length == 0)
            return "";
        if (strs.length == 1)
            return strs[0];
        StringBuffer ids = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            if (i == strs.length - 1) {
                ids.append(strs[i]);
            } else {
                ids.append(strs[i] + ",");
            }
        }
        return ids.toString();
    }

}
