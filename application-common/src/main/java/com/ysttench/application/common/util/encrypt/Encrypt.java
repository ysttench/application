package com.ysttench.application.common.util.encrypt;

import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Howard
 */
public class Encrypt {
    private static Log log = LogFactory.getLog(Encrypt.class);

    private String Algorithm = "Blowfish"; // 加密算法
    private SecretKey deskey;
    private Cipher cipher;

    public Encrypt() {
        init();
    }

    public static void main(String args[]) {
        Encrypt encrypt = new Encrypt();
        String str = "hzcominfo";
        String str2 = encrypt.encoder(str);
        String dd = encrypt.decoder(str2);
        log.info(str2);
        log.info(dd);
    }

    private void init() {
        // Security.addProvider(new SunJCE());
        deskey = new SecretKeySpec("hzcominfo".getBytes(), Algorithm);
        try {
            cipher = Cipher.getInstance(Algorithm);
        } catch (NoSuchAlgorithmException e) {
            log.error("没有此加密算法，加密器初始化失败");
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {

            log.error("加密器初始化失败");
            e.printStackTrace();
        }
    }

    /**
     * 加密
     * 
     * @param datasource
     * @return
     */
    private byte[] encoder(byte datasource[]) {
        byte encryptorData[] = (byte[]) null;
        try {
            cipher.init(1, deskey);
            encryptorData = cipher.doFinal(datasource);
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace();
        }
        return encryptorData;
    }

    /**
     * 加密
     * 
     * @param datasource
     * @return
     */
    public String encoder(String datasource) {
        byte encryptorData[] = encoder(datasource.getBytes());
        return new String((new Base64()).encode(encryptorData));
    }

    /**
     * 解密
     * 
     * @param datasource
     * @return
     */
    private byte[] decoder(byte datasource[]) {
        byte decryptorData[] = (byte[]) null;
        try {
            cipher.init(2, deskey);
            decryptorData = cipher.doFinal(datasource);
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace();
        }
        return decryptorData;
    }

    /**
     * 解密
     * 
     * @param datasource
     * @return
     */
    public String decoder(String datasource) {
        datasource = urlEncoder(datasource);
        byte decryptorData[] = new byte[datasource.length()];
        try {
            decryptorData = (new Base64()).decode(datasource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(decoder(decryptorData));
    }

    @SuppressWarnings("deprecation")
    private String urlEncoder(String datasource) {
        if (datasource.indexOf(37) < 0)
            return datasource;
        else
            return urlEncoder(URLDecoder.decode(datasource));
    }
}
