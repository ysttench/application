package com.ysttench.application.common.util.encrypt;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.DESedeKeySpec;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;


import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
*字符串 DESede(3DES) 加密
*/
public class EncodeAndDecode {
    private static Log logger = LogFactory.getLog(EncodeAndDecode.class);
    private static final String Algorithm = "DESede"; // 定义 加密算法,可用 DES,DESede,Blowfish
    private static final String hexString="0123456789ABCDEF";
    public static final String keys="TPOA_BASE64_KEY_Desede__";//加密key （长度必须为24）
     
//  static{
//      Security.addProvider(new com.sun.crypto.provider.SunJCE());
//  }
    /**
     * 获得秘密密钥
     * 
     * @param secretKey
     * @return
     * @throws NoSuchAlgorithmException 
     */
    private static SecretKey generateKey(String secretKey) throws Exception{
          
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Algorithm);
        DESedeKeySpec keySpec = new DESedeKeySpec(secretKey.getBytes("UTF-8"));
        keyFactory.generateSecret(keySpec);
        return keyFactory.generateSecret(keySpec);
    }
    /**
     *                                                   
     * @param keybyte  加密密钥，长度为24字节
     * @param src     字节数组(根据给定的字节数组构造一个密钥。 )
     * @return
     */
    public static String encryptMode(String keybyte, String src) {
        try {
            Cipher cipher = Cipher.getInstance(Algorithm);  
//          // 加密
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(keybyte));
            return Base64.encode(cipher.doFinal(src.getBytes("UTF-8")));
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param keybyte 密钥
     * @param src       需要解密的数据
     * @return
     */
    public static String decryptMode(String keybyte, String src) {
        try {
            Cipher cipher = Cipher.getInstance(Algorithm);
            // 解密
            cipher.init(Cipher.DECRYPT_MODE, generateKey(keybyte));
            return new String(cipher.doFinal(Base64.decode(src)),"UTF-8");
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 字符串转为16进制
     * @param str
     * @return
     */
    public static String encode(String str) 
    { 
        //根据默认编码获取字节数组 
        byte[] bytes=str.getBytes(); 
        StringBuilder sb=new StringBuilder(bytes.length*2); 

        //将字节数组中每个字节拆解成2位16进制整数 
        for(int i=0;i<bytes.length;i++) 
        { 
            sb.append(hexString.charAt((bytes[i]&0xf0)>>4)); 
            sb.append(hexString.charAt((bytes[i]&0x0f)>>0)); 
        } 
        return sb.toString(); 
    } 
    /**
     * 
     * @param bytes
     * @return
     * 将16进制数字解码成字符串,适用于所有字符（包括中文） 
     */ 
    public static String decode(String bytes) 
    { 
        ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2); 
        //将每2位16进制整数组装成一个字节 
        for(int i=0;i<bytes.length();i+=2) 
            baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1)))); 
        return new String(baos.toByteArray()); 
    } 

    // 转换成十六进制字符串
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = hs + ":";
        }
        return hs.toUpperCase();
    }
    
    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);

        for (i = 0; i < src.length(); i++) {

            j = src.charAt(i);

            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    /**
     * URL转码
     */
    public static String encodeStr(String str, String en) {
        try {
            return URLEncoder.encode(str, en);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * URL转码
     */
    public static String encodeStr(String str) {
        try {
            if (StringUtils.isBlank(str)) {
                str = "";
            }

            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * URL解码
     */
    public static String decodeStr(String str) {
        return decodeStr(str, "utf-8");
    }

    /**
     * URL解码
     */
    public static String decodeStr(String str, String en) {
        try {
            return URLDecoder.decode(str, en);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static void main(String[] args) throws Exception{
        // 添加新安全算法,如果用JCE就要把它添加进去
        //这里addProvider方法是增加一个新的加密算法提供者(个人理解没有找到好的答案,求补充)
        //Security.addProvider(new com.sun.crypto.provider.SunJCE());
        //byte数组(用来生成密钥的)
    //   try{
            String szSrc = "axa-ins.com.cn\\AXATPTrust";
            String pwd = "Password1";
        logger.info("加密前的字符串:" + szSrc);

        String encoded = encryptMode(keys, szSrc);

        logger.info("数组通过base64转为字符串："+encoded);
        logger.info("pwd:"+encryptMode(keys, pwd));
        logger.info("deviceNum:"+encryptMode(keys, "iphone89083942834092384"));
//      String srcBytes = decryptMode(keys,encoded);
    /*  String srcBytes = decryptMode(keys,"cyCqVSMHIgU=");
        logger.info("解密后的字符串:" + srcBytes);*/
//      byte[] srcBytes = decryptMode(keyBytes, encoded);
//      logger.info("解密后的字符串:" + new String(srcBytes));
        //------------------------------------------------------------
//      String byt="字符串转换为16进制afje";
//      byt=encode(byt);
//      logger.info("加密后字符串："+byt);
//      byt=decode(byt);
//      logger.info("解密后字符串："+byt);
         //}catch(Exception e){}
    }
    
        
//      /**
//       * 将原始数据编码为base64编码
//       */
//      static public String encodeBase64(byte[] encbyte) {
//          BASE64Encoder enc=new BASE64Encoder();
//          return enc.encode(encbyte);
//      }
//
//      /**
//       * 将base64编码的数据解码成原始数据
//       */
//      static public byte[] decodeBase64(String decString)throws Exception {
//          BASE64Decoder d=new BASE64Decoder();
//          return d.decodeBuffer(decString);
//      }
}
