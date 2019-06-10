package com.ysttench.application.common.constants;

import java.util.Locale;

/**
 * 常量接口
 * @author Howard
 *
 */
public interface BaseConstants {
    //语言环境常量
    public static final Locale DEFAULT_LOCALE = Locale.CHINA;
    //默认分页条数
    public static final int PAGE_SIZE_DEFAULT = 20;
    // 登录帐号的SessionKey
    public static final String SESSION_USER_KEY = "userSession";
    // 登录帐号的SessionKey
    public static final String SESSION_SYSTEM_KEY = "systemSession";
    //登录帐号ID的SessionKey
    public static final String SESSION_USER_ID_KEY = "userIdSession";
    //登陆账号权限的SessionKey
    public static final String SESSION_USER_ACCESS_TOKEN_KEY = "accessToken";
    //APP登录帐号SessionKey
    public static final String APP_SESSION_USER_KEY = "appSessionUserKey";
    
    //记录状态
    public static final int STATUS_ISVALID_YES = 1;
    public static final int STATUS_ISVALID_NO  = 0;
}
