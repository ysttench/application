package com.ysttench.application.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class DateJsonValueProcessor implements JsonValueProcessor {
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private DateFormat dateFormat;

    public DateJsonValueProcessor(String datePattern) {
        try {
            dateFormat = new SimpleDateFormat(datePattern);
        } catch (Exception ex) {
            dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        }
    }

    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        if(value == null){
            return "";
        }
        return process(value);
    }

    public Object processObjectValue(String key, Object value,
            JsonConfig jsonConfig) {
        if(value == null){
            return "";
        }
        return process(value);
    }

    private Object process(Object value) {
        return dateFormat.format((Date) value);
    }
}
