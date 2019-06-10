package com.ysttench.application.common.server;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ysttench.application.common.util.encrypt.EncodeAndDecode;



public class CookieUtil {
    private static Log log = LogFactory.getLog(CookieUtil.class);
    private HttpServletRequest request;
    private HttpServletResponse response;
    private int age;// 设置cookie经过多长秒后被删除

    public CookieUtil(HttpServletRequest request, HttpServletResponse response, int age) {
        this.request = request;
        this.response = response;
        this.age = age;
    }

    public void addCookie(String name, String value) {
        log.info(name + ":" + value);
        Cookie cookies = new Cookie(name, EncodeAndDecode.escape(value));
        cookies.setPath("/"+request.getContextPath());
        // cookies.setMaxAge(-1);
        // 设置cookie经过多长秒后被删除。如果0，就说明立即删除。如果是负数就表明当浏览器关闭时自动删除。
        cookies.setMaxAge(age * 24 * 3600);
        response.addCookie(cookies);
    }

    public String getCookieValue(String cookieName) {
        if (cookieName != null) {
            Cookie cookie = getCookie(cookieName);
            if (cookie != null) {
                return cookie.getValue();
            }
        }
        return "";
    }

    public Cookie getCookie(String cookieName) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        try {
            if (cookies != null && cookies.length > 0) {
                for (int i = 0; i < cookies.length; i++) {
                    cookie = cookies[i];
                    if (cookie.getName().equals(cookieName)) {
                        return cookie;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cookie;
    }

    public boolean deleteCookie(String cookieName) {
        if (cookieName != null) {
            Cookie cookie = getCookie(cookieName);
            if (cookie != null) {
                cookie.setMaxAge(0);// 如果0，就说明立即删除
                cookie.setPath("/"+request.getContextPath());// 不要漏掉
                response.addCookie(cookie);
                return true;
            }
        }
        return false;
    }

}
