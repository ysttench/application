package com.ysttench.application.common.util.encrypt;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.security.MessageDigest;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Description: 密码加密
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: www.uucun.com
 * </p>
 * @author Howard
 * @version 1.0
 */
public class Password {
    private static Log log = LogFactory.getLog(Password.class);

    public static void main(String[] args) throws Exception {
        System.out.println(digestPassword("123456"));
    }

    private static final int SALT_ORIGINAL_LEN = 8;

    private static final int SALT_ENCODED_LEN = 12;

    /**
     * @method digestPassword
     * @param password 需要加密的字符串
     * @description 加密
     */
    public static String digestPassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_ORIGINAL_LEN];
            random.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt);
            md.update(password.getBytes());
            byte[] digest = md.digest();

            Base64 base64 = new Base64();
            String s=new String(base64.encode(salt)) + new String(base64.encode(digest));
            return s;
        } catch (NoSuchAlgorithmException ne) {
            log.info(ne.toString());
            return null;
        } catch (Exception e) {
            log.info(e.toString());
            return null;
        }
    }

    /**
     * @method validatePassword
     * @param password 用户输入的明文密码，如：123456
     * @param digest 密文密码
     * @return true:密码相同 false:密码不同
     * @description 验证一个明文密码和一个密文密码是否相等ͬ
     */
    public static boolean validatePassword(String password, String digest) {
        boolean label = false;
        try {
            String salt_str = digest.substring(0, SALT_ENCODED_LEN);
            String digest_str = digest.substring(SALT_ENCODED_LEN, digest.length());
            Base64 decoder = new Base64();
            byte[] salt = decoder.decode(salt_str);
            byte[] digest_old = decoder.decode(digest_str);

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt);
            md.update(password.getBytes());
            byte[] digest_new = md.digest();
            label = Arrays.equals(digest_old, digest_new);
        } catch (NoSuchAlgorithmException ne) {
            log.info(ne.toString());
        } catch (Exception e) {
            log.info(e.toString());
        }
        return label;
    }

    /**
     * @method validateCryptograph
     * @param cryptograph 密文密码，如：iGgs9Ly5pOc=KWfGFcfCV94iZetG31SskQ==
     * @param digest 目标密文密码，如：iGgs9Ly5pOc=KWf3l33V94iZetG31a33df==
     * @return true:密码相同 false:密码不同
     * @description 验证两个同为密文的密码是否相等ͬ
     */
    public static boolean validateCryptograph(String cryptograph, String digest) {
        return true;
    }
}
