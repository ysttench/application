package com.ysttench.application.common.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解 拦截Controller
 * @author Howard
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLog {
	String module() default ""; // 模块名称 系统管理-用户管理－列表页面

    String methods() default ""; // 新增用户

    String description() default ""; //
}
