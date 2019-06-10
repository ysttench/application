package com.ysttench.application.common.util.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class File2String {
    private static Log log = LogFactory.getLog(File2String.class);
    private static String Charset = "UTF-8";
    private static String Base64Str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public String DecodeBase64(byte[] Value) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        String m = "";

        byte[] d = new byte[4];
        try {
            int count = 0;
            // byte[] x = Value.getBytes();
            byte[] x = Value;
            while (count < x.length) {
                for (int n = 0; n <= 3; n++) {
                    if (count >= x.length) {
                        d[n] = 64;
                    } else {
                        int y = Base64Str.indexOf(x[count]);
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
        } catch (StringIndexOutOfBoundsException e) {
            log.error(e.toString());
        }

        try {
            m = o.toString(File2String.Charset);
        } catch (UnsupportedEncodingException ea) {
            log.error(ea.toString());
        }

        return m;
    }

    public static String EncodeBase64(byte[] Value) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();

        byte[] d = new byte[4];
        try {
            int count = 0;

            // byte[] x = Value.getBytes(this.Charset);
            byte[] x = file2byte("");
            // byte[] x =Value;
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
                    o.write(Base64Str.charAt(d[n]));
            }
        } catch (StringIndexOutOfBoundsException e) {
            log.error(e.toString());
        }
        return o.toString();
    }

    public static byte[] file2byte(String filePath) {
        byte[] btemp = null;
        File f = new File("d:/ac.jpg");
        // File f=new File(filePath);
        if (!f.exists()) {
            log.info("文件存在");
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            btemp = bos.toByteArray();
            return btemp;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                bos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将数据流转为byte[]
     * 
     * @param is
     * @return
     */
    public static byte[] Stream2bytes(InputStream is) {
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new BufferedInputStream(is);
            out = new ByteArrayOutputStream(1024);

            log.info("Available bytes:" + in.available());

            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        byte[] content = out.toByteArray();
        return content;
    }
}
