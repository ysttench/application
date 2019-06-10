package com.ysttench.application.common.server;

import java.util.Locale;

/**
 * @author cwh created on 2009-03-30
 */
public class LocaleUtil {

    public static String getDisplayCode() {

        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return locale.getLanguage() + ("".equals(country) ? "" : "_" + country);
    }

    public static String getLanguageCode() {
        Locale locale = getUserLocale();
        return locale.getLanguage();
    }

    public static Locale getUserLocale() {
        return SessionUtil.getRequest().getLocale();
    }
}
