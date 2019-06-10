package com.ysttench.application.auth.settings.web.controller.common;
/**
 * 权限基础参数配置类
 * @author howard
 */
public class AttrConstants {
    public static final String FAIL1 = "fail1";
	/**
     * 地市行政区划编码
     */
    public static String XZHQ_CODE="C0002";
    // 重定向
    public static String ERROR = "error";
    // 成功
    public static String SUCCESS = "success";
    // 失败
    public static String FAIL = "fail";
    // 用户和角色对应异常
    public static String USER_ROLE_FAIL = "userRoleFail";
    // 分配角色异常
    public static String ROLE_FAIL = "roleFail";
    // 查询不到输入的恢复用户名
    public static String IS_NOT_EXIST = "isnotexist";
    // 查询不到输入的恢复角色名
    public static String ROLE_IS_NOT_EXIST = "isnotexist";
    // 查询不到输入的恢复系统参数key
    public static String SETTING_IS_NOT_EXIST = "isnotexist";
}
