package com.ysttench.application.common.util;

import java.util.Comparator;

public class SetUtil implements Comparator<Object> {
     public int compare(Object a, Object b) {
    
        if (Integer.valueOf((String)a) - Integer.valueOf((String)b)>0) {
            return -1;
        }
        if (Integer.valueOf((String)a) - Integer.valueOf((String)b)==0) {
            return 0;
        }
        if (Integer.valueOf((String)a) - Integer.valueOf((String)b)<0) {
            return 1;
        }
        return 0;
     }
}
