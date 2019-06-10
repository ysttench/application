package com.ysttench.application.common.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Http客户端工具类<br/>
 * 这是内部调用类，请不要在外部调用。
 * 
 * @author miklchen
 * 
 */
public class HttpClientPDAUtil {
    private static final String CONTENT_TYPE = "application/json";
    public static final String ENCODING = "UTF-8";
    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";

    private static Log logger = LogFactory.getLog(HttpClientPDAUtil.class);

    /**
     * get HttpURLConnection
     * 
     * @param strUrl
     *            url地址
     * @return HttpURLConnection
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String strUrl) throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        return httpURLConnection;
    }

    /**
     * get HttpsURLConnection
     * 
     * @param strUrl
     *            url地址
     * @return HttpsURLConnection
     * @throws IOException
     */
    public static HttpsURLConnection getHttpsURLConnection(String strUrl) throws IOException {
        URL url = new URL(strUrl);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        return httpsURLConnection;
    }

    /**
     * 获取不带查询串的url
     * 
     * @param strUrl
     * @return String
     */
    public static String getURL(String strUrl) {

        if (null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if (-1 != indexOf) {
                return strUrl.substring(0, indexOf);
            }

            return strUrl;
        }

        return strUrl;
    }

    /**
     * 获取查询串
     * 
     * @param strUrl
     * @return String
     */
    public static String getQueryString(String strUrl) {

        if (null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if (-1 != indexOf) {
                return strUrl.substring(indexOf + 1, strUrl.length());
            }

            return "";
        }

        return strUrl;
    }

    /**
     * 查询字符串转换成Map<br/>
     * name1=key1&name2=key2&...
     * 
     * @param queryString
     * @return
     */
    public static Map<String, String> queryString2Map(String queryString) {
        if (null == queryString || "".equals(queryString)) {
            return null;
        }

        Map<String, String> m = new HashMap<String, String>();
        String[] strArray = queryString.split("&");
        for (int index = 0; index < strArray.length; index++) {
            String pair = strArray[index];
            HttpClientPDAUtil.putMapByPair(pair, m);
        }

        return m;
    }

    /**
     * 把键值添加至Map<br/>
     * pair:name=value
     * 
     * @param pair
     *            name=value
     * @param m
     */
    public static void putMapByPair(String pair, Map<String, String> m) {

        if (null == pair || "".equals(pair)) {
            return;
        }

        int indexOf = pair.indexOf("=");
        if (-1 != indexOf) {
            String k = pair.substring(0, indexOf);
            String v = pair.substring(indexOf + 1, pair.length());
            if (null != k && !"".equals(k)) {
                m.put(k, v);
            }
        } else {
            m.put(pair, "");
        }
    }

    /**
     * BufferedReader转换成String<br/>
     * 注意:流关闭需要自行处理
     * 
     * @param reader
     * @return String
     * @throws IOException
     */
    public static String bufferedReader2String(BufferedReader reader) throws IOException {
        StringBuffer buf = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            buf.append(line);
            buf.append("\r\n");
        }

        return buf.toString();
    }

    /**
     * 处理输出<br/>
     * 注意:流关闭需要自行处理
     * 
     * @param out
     * @param data
     * @param len
     * @throws IOException
     */
    public static void doOutput(OutputStream out, byte[] data, int len) throws IOException {
        int dataLen = data.length;
        int off = 0;
        while (off < dataLen) {
            if (len >= dataLen) {
                out.write(data, off, dataLen);
            } else {
                out.write(data, off, len);
            }

            // 刷新缓冲区
            out.flush();

            off += len;

            dataLen -= len;
        }
    }

    /**
     * http get
     * 
     * @param charSet
     *            default utf-8
     * @return
     */
    private static String get(GetMethod get, String charSet) {
        HttpClient httpClient = new HttpClient();
        int status = 0;
        InputStream is = null;

        try {
            status = httpClient.executeMethod(get);
            is = get.getResponseBodyAsStream();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e, e);
        }
        logger.info("status :" + status);
        return getResponse(status, is, charSet);
    }

    private static String getResponse(int status, InputStream is) {
        return getResponse(status, is, null);
    }

    private static String getResponse(int status, InputStream is, String charSet) {
        String encode = StringUtils.isNotEmpty(charSet) ? charSet : HttpClientPDAUtil.ENCODING;
        StringBuilder sb = new StringBuilder();
        if (status == HttpStatus.SC_OK && is != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, encode));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.error(e, e);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e, e);
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static String getRequest(String url, List<NameValuePair> list, String charSet, String contentType) {
        String encode = StringUtils.isNotEmpty(charSet) ? charSet : HttpClientPDAUtil.ENCODING;
        String type = StringUtils.isNotEmpty(contentType) ? contentType : HttpClientPDAUtil.CONTENT_TYPE;

        String encodeUrl = getEncodeUrl(url, charSet);
        GetMethod get = new GetMethod(encodeUrl);
        get.addRequestHeader("Content-Type", type + ";charset=" + encode);

        if (list != null && !list.isEmpty()) {
            NameValuePair[] arr = new NameValuePair[list.size()];
            get.setQueryString(list.toArray(arr));
        }
        logger.debug("url=" + url);
        return get(get, encode);
    }

    private static String getEncodeUrl(String url) {
        return getEncodeUrl(url, null);
    }

    private static String getEncodeUrl(String url, String charSet) {
        String encode = StringUtils.isNotEmpty(charSet) ? charSet : HttpClientPDAUtil.ENCODING;
        String encodeUrl = url;
        try {
            encodeUrl = URIUtil.encodeQuery(url, encode);
        } catch (URIException e) {
            e.printStackTrace();
        }
        return encodeUrl;
    }

    public static String getRequest(String url, List<NameValuePair> list) {
        return request(url, HTTP_METHOD_GET, list, "");
    }

    public static String getRequest(String url, String queryString) {
        return request(url, HTTP_METHOD_GET, null, queryString);
    }

    public static String postRequest(String url, List<NameValuePair> list) {
        return request(url, HTTP_METHOD_POST, list, "");
    }

    public static String postRequest(String url, String requestBody) {
        return request(url, HTTP_METHOD_POST, null, requestBody);
    }

    private static String request(String url, String method, List<NameValuePair> list, String body) {
        if (StringUtils.isEmpty(url)) {
            return "";
        }
        if (method.equalsIgnoreCase("get")) {
            return get(url, list, body);
        } else if (method.equalsIgnoreCase("post")) {
            return post(url, list, body);
        }

        return "";
    }

    private static String post(String url, List<NameValuePair> list, String body) {
        HttpClient httpClient = new HttpClient();
        int status = 0;
        InputStream is = null;
        PostMethod post = new PostMethod(url);
        post.addRequestHeader("Content-Type", CONTENT_TYPE);
        if (list != null && !list.isEmpty()) {
            NameValuePair[] arr = new NameValuePair[list.size()];
            post.setRequestBody(list.toArray(arr));
        } else if (StringUtils.isNotEmpty(body)) {
            RequestEntity entity = null;
            try {
                entity = new StringRequestEntity(body, "", "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            post.setRequestEntity(entity);
        }
        try {
            status = httpClient.executeMethod(post);
            is = post.getResponseBodyAsStream();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e, e);
        }
        return getResponse(status, is);
    }

    private static String get(String url, List<NameValuePair> list, String body) {
        HttpClient httpClient = new HttpClient();
        int status = 0;
        InputStream is = null;
        String encodeUrl = getEncodeUrl(url);
        GetMethod get = new GetMethod(encodeUrl);
        get.addRequestHeader("Content-Type", CONTENT_TYPE);

        if (list != null && !list.isEmpty()) {
            NameValuePair[] arr = new NameValuePair[list.size()];
            get.setQueryString(list.toArray(arr));
        } else if (StringUtils.isNotEmpty(body)) {
            get.setQueryString(body);
        }
        try {
            status = httpClient.executeMethod(get);
            is = get.getResponseBodyAsStream();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e, e);
        }
        return getResponse(status, is);
    }

    public static String getRequest(String url, String queryString, String charSet, String contentType) {
        String encode = StringUtils.isNotEmpty(charSet) ? charSet : HttpClientPDAUtil.ENCODING;
        String type = StringUtils.isNotEmpty(contentType) ? contentType : HttpClientPDAUtil.CONTENT_TYPE;

        String encodeUrl = getEncodeUrl(url, encode);
        GetMethod get = new GetMethod(encodeUrl);
        get.addRequestHeader("Content-Type", type + ";charset=" + encode);

        if (StringUtils.isNotEmpty(queryString)) {
            get.setQueryString(queryString);
        }
        return get(get, encode);
    }

}
