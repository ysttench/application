package com.ysttench.application.common.util;

import java.util.Iterator;
import java.util.Map;

public class MapUtils {

    /**
     * 将字符串数组转化成中间用逗号分割的字符串 "'a','b','c'"
     * @param map
     * @return
     */
    public static String getMapKeys(Map<String, Object> map) {
        if (map == null || map.size() == 0)
            return "";
        int count = 0;
        Iterator<String> keys = map.keySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (keys.hasNext()) {
            sb.append(keys.next());
            if (count < map.size() - 1)
                sb.append(",");
            count++;
        }
        return sb.toString();
    }

    /**
     * 入力パラメータで指定されたMapがnull又はMapの元素数が"0"場合、 trueを判断結果として戻る。
     * 上記以外の場合、falseを判断結果として戻る。<br>
     * 
     * @param val
     *            Map
     * 
     * @return true:空
     */
    public static boolean isEmpty(Map<?, ?> val) {

        // ◎ valがnullの場合
        if (val == null || val.size() == 0) {
            // ○ 結果の戻る
            return true;
        }

        return false;
    }

}
