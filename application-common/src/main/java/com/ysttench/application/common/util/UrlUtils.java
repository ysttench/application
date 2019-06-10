package com.ysttench.application.common.util;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class UrlUtils {

    public static String getDataUrl(String urlStr, Map < String, String > paramMap) throws Exception{
        String param = "";
        int i = 0;
        if (paramMap.size() > 0) {
            Set < String > keySet = paramMap.keySet();
            for (Object key : keySet) {
                char c = '&';
                String keyValue = String.valueOf(key);
                String value = (String) paramMap.get(keyValue);
                if (value == null) {
                    value = "";
                }
                if (i == 0) {
                    c = '?';
                }
                param += (c + keyValue + "=" + value);
                i++;
            }
        }

        return urlStr + param;
    }
    
    /**
     * 循环删除最后的某个字符，至不是以该字符结尾
     * 
     * @param value
     * @return
     */
    public static String removeEnd(String value, char c) {

        if (StringUtils.isBlank(value)) {
            return "";
        }
        String ret = value;
//        int i = 0;
        while (StringUtils.isNotBlank(ret)
                && (StringUtils.lastIndexOf(ret, c) == (ret.length() - 1))) {
            ret = StringUtils.removeEnd(ret, String.valueOf(c));
        }
        return ret;
    }

    public static String removeStart(String value, char c) {
        if (StringUtils.isBlank(value)) {
            return "";
        }

        String ret = value;
//        int i = 0;
        while (StringUtils.isNotBlank(ret)
                && (StringUtils.indexOf(ret, String.valueOf(c)) == 0)) {
            ret = StringUtils.removeStart(ret, String.valueOf(c));
        }
        return ret;
    }

    public static String removeFirstAndEnd(String value, char c) {
        String ret = removeEnd(value, c);
        return removeStart(ret, c);
    }

    /**
     * 处理Url与其参数的组合
     * 
     * @param url页面Url
     * @param param被加入到该Url后的参数
     * @return 一个完整的Url,包括参数
     */
    public static String dealUrl(String url, String param) {
        String orgUrl = url;
        url = removeEnd(url, '#');
        url = removeEnd(url, '?');
        if (StringUtils.isBlank(url)) {
            return "";
        }

        if (url.lastIndexOf('/') == (url.length() - 1)) {
            url += "index.html";
        }

        if (StringUtils.isBlank(param)) {
            return orgUrl;
        }

        param = removeStart(param, '&');
        param = removeStart(param, '?');
        if (StringUtils.isBlank(param)
                || (param.indexOf("=") == -1)
                || (param.indexOf("=") > 0 && (param.indexOf("=") == (param
                        .length() - 1)))) {
            return url;
        }
        if (url.indexOf("?") > 0) {
            url += "&" + param;
        } else {
            url += "?" + param;
        }
        return url;
    }

    public static String deleteUrlParam(String url, String key) {
        if (url == null || url.length() < 1)
            return "";
        url = url.trim();
        if (key == null || key.length() < 1)
            return url;
        key = key.trim();

        return "";
    }

    public static ClassLoader getStandardClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static ClassLoader getFallbackClassLoader() {
        return Utils.class.getClassLoader();
    }

    /**
     * 得到配置文件的URL
     * 
     * @param name
     * @return 配置文件URL
     * 
     */
    public static URL getConfURL(String name) {
        try {
            return getStandardClassLoader().getResource(name);
        } catch (Exception e) {
            try {
                return getFallbackClassLoader().getResource(name);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getWebInfPath(Object t) {
        String path = t.getClass().getProtectionDomain().getCodeSource()
                .getLocation().getPath();
        return path.substring(0, path.indexOf("WEB-INF") + 8);
    }

    /**
     * TODO 获取根目录
     * 
     * @return
     * @author PHeH
     * 
     *         Created On 2007-5-10 15:16:21
     */
    public static String getRootPath() {
        // 因为类名为"Application"，因此" Application.class"一定能找到
        String result = UrlUtils.class.getResource("UrlUtils.class").toString();
        int index = result.indexOf("WEB-INF");
        if (index == -1) {
            index = result.indexOf("bin");
        }
        result = result.substring(0, index);
        if (result.startsWith("jar")) {
            // 当class文件在jar文件中时，返回"jar:file:/F:/ ..."样的路径
            result = result.substring(10);
        } else if (result.startsWith("file")) {
            // 当class文件在class文件中时，返回"file:/F:/ ..."样的路径
            result = result.substring(6);
        }
        if (result.endsWith("/"))
            result = result.substring(0, result.length() - 1);// 不包含最后的"/"
        return result;
    }

    /**
     * TODO 获取运行系统根目录
     * 
     * @return
     * @author PHeH
     * 
     *         Created On 2007-5-10 15:16:21
     */
    public static String getSysRootPath() {
        // 因为类名为"Application"，因此" Application.class"一定能找到
        String result = UrlUtils.class.getResource("UrlUtils.class").toString();

        if (isWindows()) {
            if (result.startsWith("jar")) {
                // 当class文件在jar文件中时，返回"jar:file:/F:/ ..."样的路径
                return result.substring(10, 12);
            } else if (result.startsWith("file")) {
                System.out.println(result);
                // 当class文件在class文件中时，返回"file:/F:/ ..."样的路径
                return result.substring(6, 8);
            } else {
                return result.substring(0, 2);
            }
        }
        return "";
    }

    private static boolean isWindows() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return true;
        }
        return false;
    }
    /**
     * 获取文件全路径
     * @param savePath
     * @param outputFile
     * @return
     */
    public static String getFileFullPathForRandomFileName(String savePath, String outputFile) {
        String message;
        File file = new File(savePath);
        // 判断上传文件的保存目录是否存在
        if (!file.exists() && !file.isDirectory()) {
            // logger.info(savePath+"目录不存在，需要创建");
            // 创建目录
            file.mkdirs();
        }
        // 生成文件名
        String name = UUID.randomUUID().toString() + outputFile.substring(outputFile.lastIndexOf("."));
        message = savePath + File.separatorChar + name.replace("\\", "/");
        return message;
    }

    /**
     * 获取文件全路径
     * @param savePath
     * @param fileName
     * @param outputFile
     * @return
     */
    public static String getFileFullPath(String savePath, String fileName, String outputFile) {
        String message;
        File file = new File(savePath);
        // 判断上传文件的保存目录是否存在
        if (!file.exists() && !file.isDirectory()) {
            // logger.info(savePath+"目录不存在，需要创建");
            // 创建目录
            file.mkdirs();
        }
        // 生成文件名
        String name = fileName + outputFile.substring(outputFile.lastIndexOf("."));

        message = savePath +  File.separatorChar  + name.replace("\\", "/");
        return message;
    }

    /**
     * 获取文件全路径
     * @param savePath
     * @param fileName
     * @param outputFile
     * @return
     */
    public static String getFileFullPath(String savePath, String fileName) {
        String message;
        File file = new File(savePath);
        // 判断上传文件的保存目录是否存在
        if (!file.exists() && !file.isDirectory()) {
            // logger.info(savePath+"目录不存在，需要创建");
            // 创建目录
            file.mkdirs();
        }
        // 生成文件名
        message = savePath +  File.separatorChar  + fileName.replace("\\", "/");
        return message;
    }

    /**
     * 获取文件全路径
     * @param savePath
     * @param fileName
     * @param outputFile
     * @return
     */
    public static void isExistsPath(String savePath) {
        File file = new File(savePath);
        // 判断上传文件的保存目录是否存在
        if (!file.exists() && !file.isDirectory()) {
            // logger.info(savePath+"目录不存在，需要创建");
            // 创建目录
            file.mkdirs();
        }
    }

    /**
     * 返回用户的IP地址
     * 
     * @param request
     * @return
     */
    public static String toIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
