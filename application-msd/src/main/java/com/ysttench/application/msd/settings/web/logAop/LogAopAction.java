package com.ysttench.application.msd.settings.web.logAop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.HtmlUtil;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.msd.settings.kernel.entity.LogOperateFormMap;
import com.ysttench.application.msd.settings.kernel.entity.SysUserFormMap;
import com.ysttench.application.msd.settings.kernel.mapper.LogOperateMapper;

/**
 * 切点类
 */
@Aspect
@Component
public class LogAopAction {
    // 本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(LogAopAction.class);
    @Inject
    private LogOperateMapper logMapper;

    // Controller层切点
    @Pointcut("@annotation(com.ysttench.application.common.annotation.SystemLog)")
    public void controllerAspect() {
    }


    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint point, Throwable e) {
        LogOperateFormMap logForm = new LogOperateFormMap();
        Map<String, Object> map = getControllerMethodDescription(point);
        String user = getUserName();
        String ip = getUserIpAddress();
        JSONObject object = SessionUtil.getJsonParam();
        logForm.put("userName", user);
        logForm.put("module", map.get("MODULE"));
        logForm.put("method", "<font color=\"red\">执行方法异常:-->" + map.get("METHOD") + "</font>");
        logForm.put("requestParam", object);
        logForm.put("description", HtmlUtil.htmltoString("<font color=\"red\">执行方法异常:-->" + e + "</font>"));
        logForm.put("actionTime", "0");
        logForm.put("userIpAddress", ip);
        try {
            logMapper.addEntity(logForm);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     * 
     * @param joinPoint
     *            切点
     */
    @Around("controllerAspect()")
    public Object doController(ProceedingJoinPoint point) {
        // 执行方法名
         String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getSimpleName();
        LogOperateFormMap logForm = new LogOperateFormMap();
        Map<String, Object> map = getControllerMethodDescription(point);
        String user = getUserName();
        String ip = getUserIpAddress();
        
        Object[] result = getActionTime(point);
        JSONObject object = SessionUtil.getJsonParam();
        try {
            logForm.put("userName", user);
            logForm.put("module", map.get("MODULE"));
            logForm.put("method", map.get("METHOD"));
            logForm.put("requestParam", object);
            logForm.put("description", map.get("DESCREIPTION"));
            logForm.put("actionTime", result[1].toString());
            logForm.put("userIpAddress", ip);
            logMapper.addEntity(logForm);
            // *========控制台输出=========*//
            System.out.println("=====通知开始=====");
            System.out.println("请求方法:" + className + "." + methodName + "()");
            System.out.println("方法描述:" + map);
            System.out.println("请求IP:" + ip);
            System.out.println("=====通知结束=====");
        } catch (Exception e) {
            // 记录本地异常日志
            logger.error("====通知异常====");
            logger.error("异常信息:{}", e.getMessage());
        }
        return result[0];
    }
    
    /**
     * 获取执行前后时间
     * @param point
     * @param time
     * @return
     */
    private Object[] getActionTime(ProceedingJoinPoint point) {
        Object[] result = new Object[2];
        Long time = 0L;
        Long start = 0L;
        Long end = 0L;
        // 当前用户
        try {
            // 执行方法所消耗的时间
            start = System.currentTimeMillis();
            result[0] = point.proceed();
            end = System.currentTimeMillis();
            time = end - start;
            result[1] = time;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 获取用户IP
     * @return
     */
    private String getUserIpAddress() {
        String ip;
        try {
            ip = SessionUtil.getIpAddress();
        } catch (Exception ee) {
            ip = "无法获取登录用户Ip";
        }
        return ip;
    }

    /**
     * 获取用户登录名
     * @return
     */
    private String getUserName() {
        String user;
        try {
            // 登录名
            user = ((SysUserFormMap)SessionUtil.getUserSession()).getStr("userName");
            if (StringUtil.isEmpty(user)) {
                user = "无法获取登录用户信息！";
            }
        } catch (Exception e) {
            user = "无法获取登录用户信息！";
        }
        return user;
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     * 
     * @param joinPoint
     *            切点
     * @return 方法描述
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public Map<String, Object> getControllerMethodDescription(JoinPoint joinPoint) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        map.put("MODULE", method.getAnnotation(SystemLog.class).module());
                        map.put("METHOD", method.getAnnotation(SystemLog.class).methods());
                        String de = method.getAnnotation(SystemLog.class).description();
                        if (StringUtil.isEmpty(de))
                            de = "执行成功!";
                        map.put("DESCREIPTION", de);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
