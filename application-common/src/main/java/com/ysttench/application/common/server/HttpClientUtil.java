package com.ysttench.application.common.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

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
public class HttpClientUtil {
    private static final String CONTENT_TYPE = "text/html;charset=UTF-8";
    public static final String ENCODING = "UTF-8";
    public static final String CHARSET_GBK = "GBK";
    public static final String CONTENT_TYPE_TEXT_HTML = "text/html";
    public static final String CONTENT_TYPE_APPLICATION_FORM = "application/x-www-form-urlencoded";
    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";

    private static Log logger = LogFactory.getLog(HttpClientUtil.class);

    public static final String SunX509 = "SunX509";
    public static final String JKS = "JKS";
    public static final String PKCS12 = "PKCS12";
    public static final String TLS = "TLS";

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
            HttpClientUtil.putMapByPair(pair, m);
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
     * 获取SSLContext
     * 
     * @param trustFile
     * @param trustPasswd
     * @param keyFile
     * @param keyPasswd
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public static SSLContext getSSLContext(FileInputStream trustFileInputStream, String trustPasswd,
            FileInputStream keyFileInputStream, String keyPasswd) throws NoSuchAlgorithmException, KeyStoreException,
            CertificateException, IOException, UnrecoverableKeyException, KeyManagementException {

        // ca
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(HttpClientUtil.SunX509);
        KeyStore trustKeyStore = KeyStore.getInstance(HttpClientUtil.JKS);
        trustKeyStore.load(trustFileInputStream, HttpClientUtil.str2CharArray(trustPasswd));
        tmf.init(trustKeyStore);

        final char[] kp = HttpClientUtil.str2CharArray(keyPasswd);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(HttpClientUtil.SunX509);
        KeyStore ks = KeyStore.getInstance(HttpClientUtil.PKCS12);
        ks.load(keyFileInputStream, kp);
        kmf.init(ks, kp);

        SecureRandom rand = new SecureRandom();
        SSLContext ctx = SSLContext.getInstance(HttpClientUtil.TLS);
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);

        return ctx;
    }

    /**
     * 获取CA证书信息
     * 
     * @param cafile
     *            CA证书文件
     * @return Certificate
     * @throws CertificateException
     * @throws IOException
     */
    public static Certificate getCertificate(File cafile) throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in = new FileInputStream(cafile);
        Certificate cert = cf.generateCertificate(in);
        in.close();
        return cert;
    }

    /**
     * 字符串转换成char数组
     * 
     * @param str
     * @return char[]
     */
    public static char[] str2CharArray(String str) {
        if (null == str)
            return null;

        return str.toCharArray();
    }

    /**
     * 存储ca证书成JKS格式
     * 
     * @param cert
     * @param alias
     * @param password
     * @param out
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     */
    public static void storeCACert(Certificate cert, String alias, String password, OutputStream out)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance("JKS");

        ks.load(null, null);

        ks.setCertificateEntry(alias, cert);

        // store keystore
        ks.store(out, HttpClientUtil.str2CharArray(password));
    }

    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
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
        String encode = StringUtils.isNotEmpty(charSet) ? charSet : HttpClientUtil.ENCODING;
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
        String encode = StringUtils.isNotEmpty(charSet) ? charSet : HttpClientUtil.ENCODING;
        String type = StringUtils.isNotEmpty(contentType) ? contentType : HttpClientUtil.CONTENT_TYPE_TEXT_HTML;

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
        String encode = StringUtils.isNotEmpty(charSet) ? charSet : HttpClientUtil.ENCODING;
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
            // post.setRequestBody(body);
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
        String encode = StringUtils.isNotEmpty(charSet) ? charSet : HttpClientUtil.ENCODING;
        String type = StringUtils.isNotEmpty(contentType) ? contentType : HttpClientUtil.CONTENT_TYPE_TEXT_HTML;

        String encodeUrl = getEncodeUrl(url, encode);
        GetMethod get = new GetMethod(encodeUrl);
        get.addRequestHeader("Content-Type", type + ";charset=" + encode);

        if (StringUtils.isNotEmpty(queryString)) {
            get.setQueryString(queryString);
        }
        return get(get, encode);
    }

}
