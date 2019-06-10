package com.ysttench.application.common.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class HttpServletUtil {
    private static Log logger = LogFactory.getLog(HttpServletUtil.class);

    /***
     * 写List
     * 
     * @param request
     * @param response
     * @param obj
     * @throws IOException
     */
    public static void writeArray(HttpServletResponse response, Object obj) {
        try{
            JSONArray json = JSONArray.fromObject(obj);
            response.setContentType("text/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(json);
            response.getWriter().flush();
            response.getWriter().close();
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    /**
     * 写Object
     * 
     * @param request
     * @param response
     * @param obj
     * @throws IOException
     */
    public static void writeObject(HttpServletResponse response, Object obj) {
        try{
            JSONObject json = JSONObject.fromObject(obj);
            response.setContentType("text/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(json);
            response.getWriter().flush();
            response.getWriter().close();
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
   }
    
    /**
     * ajax反馈
     * @param response
     * @param responseString  response反馈信息
     */
    public static void writeString(HttpServletResponse response,String responseString){
        response.setContentType("text/html;charset=UTF-8");
        try{
            PrintWriter writer = response.getWriter();
            writer.print(responseString);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    /**
     * 写单参数
     * 
     * @param request
     * @param response
     * @param obj
     * @throws IOException
     */
    public static void writeMath(HttpServletResponse response, Object obj) {
        try{
            String json = "{\"success\":\"" + obj + "\"}";
            response.setContentType("text/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(json);
            response.getWriter().flush();
            response.getWriter().close();
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public static void writeXsl(HttpServletResponse response, Object obj) {
        try{
            String json = "{\"success\":\"" + obj + "\"}";
            response.setContentType("application/msexcel");
            response.setHeader("Content-disposition", "inline;filename=" + obj);
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(json);
            response.getWriter().flush();
            response.getWriter().close();
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    /**
     * 跳转-请求转发
     * 
     * @param url
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public static void forward(String url, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        getViewPath(request);
//        request.setAttribute("viewPath", getViewPath(request));
        request.getRequestDispatcher(url).forward(request, response);
        return;
    }

    /**
     * 重定向跳转
     * 
     * @param url
     * @param request
     * @param response
     * @throws IOException
     */
    public static void sendRedirect(String url, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setAttribute("viewPath", getViewPath(request));
        response.sendRedirect(url);
        return;
    }

    /**
     * 本地测试路径设定
     * @param request
     * @return
     */
    private static String getViewPath(HttpServletRequest request) {
        if ("localhost".equals(request.getServerName().toLowerCase())
                || "127.0.0.1".equals(request.getServerName().toLowerCase())) {
            return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + request.getContextPath() + "/marketing";
        } else {
            return "..";
        }
    }
}
