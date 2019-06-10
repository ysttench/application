package com.ysttench.application.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class TreeSetUtil implements Comparator<Object> {
    public int compare(Object a, Object b) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        Date date1 = null ;
        Date date2=null;
        try {
            date1 = simpleDateFormat.parse((String)a);
            date2 = simpleDateFormat.parse((String)b);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date1 == null && date2 == null) return 0;
        if (date1 == null) return 1;
        if (date2 == null) return -1;
        if ((date1.getTime() - date2.getTime()) > 0) {
            return -1;
        }
        if ((date1.getTime() - date2.getTime()) == 0) {
            return 0;
        }
        if ((date1.getTime() - date2.getTime()) < 0) {
            return 1;
        }
        return 0;
     }
}
