package com.ysttench.application.common.util.file;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileDetailUtil {
    private static Log log = LogFactory.getLog(FileDetailUtil.class);
    private static String Base64Str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public static byte[] DecodeBase64(String Value) {
        byte[] content = null;
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        byte[] d = new byte[4];
        try {
            int count = 0;
            byte[] x = Value.getBytes();
            while (count < x.length) {
                for (int n = 0; n <= 3; n++) {
                    if (count >= x.length) {
                        d[n] = 64;
                    } else {
                        int y = FileDetailUtil.Base64Str.indexOf(x[count]);
                        if (y < 0) {
                            y = 65;
                        }
                        d[n] = (byte) y;
                    }
                    count++;
                }
                o.write((byte) (((d[0] & 0x3F) << 2) + ((d[1] & 0x30) >> 4)));
                if (d[2] != 64) {
                    o.write((byte) (((d[1] & 0xF) << 4) + ((d[2] & 0x3C) >> 2)));
                    if (d[3] != 64)
                        o.write((byte) (((d[2] & 0x3) << 6) + (d[3] & 0x3F)));
                }
            }
            content = o.toByteArray();
        } catch (StringIndexOutOfBoundsException e) {
        } finally {
            try {
                o.flush();
                o.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public static String EncodeBase64(byte[] Value) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();

        byte[] d = new byte[4];
        try {
            int count = 0;

            // byte[] x = Value.getBytes(this.Charset);
            // byte[] x =file2byte("");
            byte[] x = Value;
            while (count < x.length) {
                byte c = x[count];
                count++;
                d[0] = (byte) ((c & 0xFC) >> 2);
                d[1] = (byte) ((c & 0x3) << 4);
                if (count < x.length) {
                    c = x[count];
                    count++;
                    d[1] = (byte) (d[1] + (byte) ((c & 0xF0) >> 4));
                    d[2] = (byte) ((c & 0xF) << 2);
                    if (count < x.length) {
                        c = x[count];
                        count++;
                        d[2] = (byte) (d[2] + ((c & 0xC0) >> 6));
                        d[3] = (byte) (c & 0x3F);
                    } else {
                        d[3] = 64;
                    }
                } else {
                    d[2] = 64;
                    d[3] = 64;
                }
                for (int n = 0; n <= 3; n++)
                    o.write(FileDetailUtil.Base64Str.charAt(d[n]));
            }
        } catch (StringIndexOutOfBoundsException e) {
            log.error(e.toString());
        }
        return o.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String source = "测试数据123xiaom-=adafdaf!@#$%^";
        String tes = EncodeBase64(source.getBytes());

        log.info("tes:" + tes);

        byte[] s = DecodeBase64("6aKd5Zyw5pa5");

        log.info("des:" + new String(s, "utf-8"));
    }

    /*
     * private static char[] base64EncodeChars = new char[] { 'A', 'B', 'C',
     * 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
     * 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
     * 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
     * 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
     * '7', '8', '9', '+', '/' }; private static byte[] base64DecodeChars = new
     * byte[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
     * -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
     * -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55,
     * 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6,
     * 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
     * -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
     * 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1,
     * -1 };
     * 
     * 
     * // public static void main(String[] args) { // String string="徐忠明"; //
     * String encodeStr=encode(string.getBytes()); // byte[] bytesStr=null; //
     * try { // bytesStr=decode(encodeStr); // } catch
     * (UnsupportedEncodingException e) { // // TODO Auto-generated catch block
     * // e.printStackTrace(); // } // // System.out.println(encodeStr); //
     * System.out.println(new String(bytesStr)); // // } // 编码 public static
     * String encode(byte[] data) { StringBuffer sb = new StringBuffer(); int
     * len = data.length; int i = 0; int b1, b2, b3; while (i < len) { b1 =
     * data[i++] & 0xff; if (i == len) { sb.append(base64EncodeChars[b1 >>> 2]);
     * sb.append(base64EncodeChars[(b1 & 0x3) << 4]); sb.append("=="); break; }
     * b2 = data[i++] & 0xff; if (i == len) { sb.append(base64EncodeChars[b1 >>>
     * 2]); sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>>
     * 4)]); sb.append(base64EncodeChars[(b2 & 0x0f) << 2]); sb.append("=");
     * break; } b3 = data[i++] & 0xff; sb.append(base64EncodeChars[b1 >>> 2]);
     * sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
     * sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
     * sb.append(base64EncodeChars[b3 & 0x3f]); } return sb.toString(); } // 解码
     * public static byte[] decode(String str) throws
     * UnsupportedEncodingException { StringBuffer sb = new StringBuffer();
     * byte[] data = str.getBytes("US-ASCII"); int len = data.length; int i = 0;
     * int b1, b2, b3, b4; while (i < len) {
     * 
     * do { b1 = base64DecodeChars[data[i++]]; } while (i < len && b1 == -1); if
     * (b1 == -1) break;
     * 
     * do { b2 = base64DecodeChars[data[i++]]; } while (i < len && b2 == -1); if
     * (b2 == -1) break; sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
     * 
     * do { b3 = data[i++]; if (b3 == 61) return
     * sb.toString().getBytes("iso8859-1"); b3 = base64DecodeChars[b3]; } while
     * (i < len && b3 == -1); if (b3 == -1) break; sb.append((char) (((b2 &
     * 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
     * 
     * do { b4 = data[i++]; if (b4 == 61) return
     * sb.toString().getBytes("iso8859-1"); b4 = base64DecodeChars[b4]; } while
     * (i < len && b4 == -1); if (b4 == -1) break; sb.append((char) (((b3 &
     * 0x03) << 6) | b4)); } return sb.toString().getBytes("iso8859-1"); }
     */
}
