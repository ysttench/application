package com.ysttench.application.common.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    /**
     * 用来去掉List中空值和相同项的。
     * 
     * @param list
     * @return
     */
    public static List<String> removeSameItem(List<String> list) {
        List<String> difList = new ArrayList<String>();
        for (String t : list) {
            if (t != null && !difList.contains(t)) {
                difList.add(t);
            }
        }
        return difList;
    }

    /**
     * 字符串集合转字符串数组
     * 
     * @param list
     * @return
     */
    public static String[] list2strArry(List<String> list) {
        String[] strs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strs[i] = list.get(i);
        }
        return strs;
    }

    /**
     * 字符串数组转换为集合
     * 
     * @param strArr
     * @return
     */
    public static List<String> arr2list(String[] strArr) {
        if (strArr.length > 0) {
            List<String> list = new ArrayList<String>(strArr.length);
            for (String str : strArr) {
                list.add(str);
            }
            return list;
        } else {
            return new ArrayList<String>();
        }
    }

    /**
     * 入力パラメータで指定されたリストがnull又はリストの元素数が"0"場合、 trueを判断結果として戻る。
     * 上記以外の場合、falseを判断結果として戻る。<br>
     * 
     * @param val
     *            判定する値
     * 
     * @return true:空
     */
    public static boolean isEmpty(List<?> val) {
        // ◎ List長さの判断
        if (val == null || val.size() == 0) {
            // ○ 結果の戻る
            return true;
        }

        return false;
    }

}
