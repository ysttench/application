package com.ysttench.application.common.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HtmlUtil {

    /**
     * 取得html网页内容 UTF8编码
     * 
     * @param urlStr
     *            网络地址
     * @return String
     */
    public static String getInputHtmlUTF8(String urlStr) {
        URL url = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();

            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setConnectTimeout(5 * 1000);
            httpsURLConnection.connect();
            if (httpsURLConnection.getResponseCode() == 200) {
                // 通过输入流获取网络图片
                InputStream inputStream = httpsURLConnection.getInputStream();
                String data = readHtml(inputStream, "UTF-8");
                inputStream.close();
                return data;
            }
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }

        return null;

    }

    /**
     * 取得html网页内容 GBK编码
     * 
     * @param urlStr
     *            网络地址
     * @return String
     */
    public static String getInputHtmlGBK(String urlStr) {
        URL url = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();

            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setConnectTimeout(5 * 1000);
            httpsURLConnection.connect();
            if (httpsURLConnection.getResponseCode() == 200) {
                // 通过输入流获取网络图片
                InputStream inputStream = httpsURLConnection.getInputStream();
                String data = readHtml(inputStream, "GBK");
                inputStream.close();
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return null;

    }

    /**
     * @param inputStream
     * @param uncode
     *            编码 GBK 或 UTF-8
     * @return
     * @throws Exception
     */
    public static String readHtml(InputStream inputStream, String uncode) throws Exception {
        InputStreamReader input = new InputStreamReader(inputStream, uncode);
        BufferedReader bufReader = new BufferedReader(input);
        String line = "";
        StringBuilder contentBuf = new StringBuilder();
        while ((line = bufReader.readLine()) != null) {
            contentBuf.append(line);
        }
        return contentBuf.toString();
    }

    /**
     * html转议
     * 
     * @descript
     * @param content
     * @return
     * @author LJN
     * @date 2015年4月27日
     * @version 1.0
     */
    public static String htmltoString(String content) {
        if (content == null)
            return "";
        String html = content;
        html = html.replaceAll("&", "&amp;");
        html = html.replace("'", "&apos;");
        html = html.replace("\"", "&quot;"); // "
        html = html.replace("\t", "&nbsp;&nbsp;");// 替换跳格
        html = html.replace(" ", "&nbsp;");// 替换空格
        html = html.replace("<", "&lt;");
        html = html.replaceAll(">", "&gt;");

        return html;
    }

    /**
     * html转议
     * 
     * @descript
     * @param content
     * @return
     * @author LJN
     * @date 2015年4月27日
     * @version 1.0
     */
    public static String stringtohtml(String content) {
        if (content == null)
            return "";
        String html = content;
        html = html.replace("&apos;", "'");
        html = html.replace("&quot;", "\""); // "
        html = html.replace("&nbsp;&nbsp;", "\t");// 替换跳格
        html = html.replace("&nbsp;", " ");// 替换空格
        html = html.replace("&lt;", "<");
        html = html.replaceAll("&gt;", ">");
        html = html.replaceAll("&amp;", "&");

        return html;
    }

}
