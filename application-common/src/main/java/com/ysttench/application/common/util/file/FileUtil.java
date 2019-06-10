package com.ysttench.application.common.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.common.util.encrypt.Base64;


public class FileUtil {
    private static Logger logger = Logger.getLogger(FileUtil.class);
    private static String CHAR_SET = "UTF-8";
    private static String BASE64_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    /**
     * 
     * @param path
     *            文件路径
     * @param regex
     *            文件后缀名
     */
    public static LinkedList<String> getFiles(File file, String regex, LinkedList<String> list) {
        if (file.isDirectory()) {
            File[] filelist = file.listFiles();
            for (int index = 0, length = filelist.length; index < length; index++) {
                if (filelist[index].isDirectory()) {
                    getFiles(filelist[index], regex, list);
                } else {
                    // if(regex != null && !"".equals(regex)&&
                    // file.getName().endsWith(regex)){
                    list.add(filelist[index].getName());
                    // }
                }
            }
        } else {
            // if(regex != null && !"".equals(regex)&&
            // file.getName().endsWith(regex)){
            list.add(file.getName());
            // }
        }
        return list;
    }

    public static void writerImage(String url, String fileName, String content) {
        String separator = System.getProperty("file.separator");
        File file = new File(url + separator + fileName);
        FileWriter fileWriter = null;
        try {

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileWriter != null)
                    fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 转换文件后缀 标准
     * 
     * @param suffix
     * @return
     */
    public static String getContentType(String suffix) {
        // 文本类
        if ("txt".equals(suffix.toLowerCase()) || "asc".equals(suffix.toLowerCase())
                || "sql".equals(suffix.toLowerCase()) || "log".equals(suffix.toLowerCase()))
            return "text/plain";
        else if ("css".equals(suffix.toLowerCase()))
            return "text/css";
        else if ("html".equals(suffix.toLowerCase()) || "htm".equals(suffix.toLowerCase()))
            return "text/html";
        // 文档类
        else if ("pdf".equals(suffix.toLowerCase()))
            return "application/pdf";
        else if ("doc".equals(suffix.toLowerCase()) || "dot".equals(suffix.toLowerCase()))
            return "application/msword";
        else if ("rtf".equals(suffix.toLowerCase()))
            return "text/rtf";
        else if ("xls".equals(suffix.toLowerCase()))
            return "application/vnd.ms-excel";
        else if ("ppt".equals(suffix.toLowerCase()) || "pps".equals(suffix.toLowerCase()))
            return "application/vnd.ms-powerpoint";
        // 图片类
        else if ("bmp".equals(suffix.toLowerCase()))
            return "image/bmp";
        else if ("gif".equals(suffix.toLowerCase()))
            return "image/gif";
        else if ("ief".equals(suffix.toLowerCase()))
            return "image/ief";
        else if ("jpeg".equals(suffix.toLowerCase()))
            return "image/jpeg";
        else if ("jpg".equals(suffix.toLowerCase()))
            return "image/jpg";
        else if ("jpe".equals(suffix.toLowerCase()))
            return "image/jpeg";
        else if ("png".equals(suffix.toLowerCase()))
            return "image/png";
        else if ("tiff".equals(suffix.toLowerCase()))
            return "image/tiff";
        else if ("tif".equals(suffix.toLowerCase()))
            return "image/tif";
        // 压缩文件
        else if ("zip".equals(suffix.toLowerCase()))
            return "application/zip";
        // 音频类
        else if ("mpga".equals(suffix.toLowerCase()) || "mp2".equals(suffix.toLowerCase())
                || "mp3".equals(suffix.toLowerCase()))
            return "audio/mpeg";
        else if ("mid".equals(suffix.toLowerCase()) || "midi".equals(suffix.toLowerCase())
                || "kar".equals(suffix.toLowerCase()))
            return "audio/midi";
        else if ("aif".equals(suffix.toLowerCase()) || "aiff".equals(suffix.toLowerCase())
                || "aifc".equals(suffix.toLowerCase()))
            return "audio/x-aiff";
        else if ("au".equals(suffix.toLowerCase()) || "snd".equals(suffix.toLowerCase()))
            return "audio/basic";
        else if ("7z".equals(suffix.toLowerCase()))
            return "application/x-7z-compressed";
        else if ("docm".equals(suffix.toLowerCase()))
            return "application/vnd.ms-word.document.macroEnabled.12";
        else if ("docx".equals(suffix.toLowerCase()))
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        else if ("eml".equals(suffix.toLowerCase()))
            return "message/rfc822";
        else if ("emz".equals(suffix.toLowerCase()))
            return "application/octet-stream";
        else if ("exe".equals(suffix.toLowerCase()))
            return "application/exe";
        else if ("mht".equals(suffix.toLowerCase()))
            return "message/rfc822";
        else if ("pptx".equals(suffix.toLowerCase()))
            return "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        else if ("rar".equals(suffix.toLowerCase()))
            return "application/x-rar-compressed";
        else if ("xlsm".equals(suffix.toLowerCase()))
            return "application/vnd.ms-excel.sheet.macroEnabled.12";
        else if ("xlsx".equals(suffix.toLowerCase()))
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        else if ("xml".equals(suffix.toLowerCase()))
            return "text/xml";
        return "application/octet-stream";
    }

    public static String getWebInfPath(Object t) {
        String path = t.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        return path.substring(0, path.indexOf("WEB-INF") + 8);
    }

    public static boolean filemkdirs(String filepath) {
        File file = new File(filepath);// 构造文件
        if (!file.exists())
            return file.mkdirs();
        return true;
    }

    public static byte[] getBytesFromFile(File file) throws IOException {

        InputStream is = new FileInputStream(file);
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            is.close();
            // 文件太大，无法读取
            throw new IOException("File is to large " + file.getName());
        }
        // 创建一个数据来保存文件数据
        byte[] bytes = new byte[(int) length];
        // 读取数据到byte数组中
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset < bytes.length) {
            is.close();
            throw new IOException("Could not completely read file " + file.getName());
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;

    }

    public static File fileWrite(String filepath, byte[] content) throws IOException {
        File file = new File(filepath);// 构造文件

        InputStream ins = new ByteArrayInputStream(content);
        OutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = ins.read(b)) != -1) {
                fout.write(b, 0, len);
            }
            // return "success";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null)
                fout.close();
            if (ins != null)
                ins.close();
        }
        return file;
    }

    /**
     * 获取文件类型
     * 
     * @param fileName
     *            文件名
     * @return 返回文件类型
     */
    public static String getFileType(String filename) {
        return filename.substring(filename.lastIndexOf("."), filename.length());
    }

    /**
     * 获取文件名字，不包括文件的格式
     * 
     * @param fileName
     *            文件名
     * @return 返回文件名【不包括文件扩展名比如:t_y.est.jpg 返回t_y.est】
     */
    public static String getFileName(String filename) {
        return filename.substring(0, filename.lastIndexOf("."));
    }

    /**
     * 获取文件名称不带后缀
     * 
     * @param filename
     *            原始文件名称
     * @return
     */
    public static String getFileNameWithOutSuffix(String filename) {
        if (StringUtil.isEmptyString(filename)) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1) {
                String temp = filename.substring(0, dot);
                if (temp.lastIndexOf("\\") > 0) {
                    int index = temp.lastIndexOf("\\");
                    temp = temp.substring(index + 1);
                }
                if (temp.lastIndexOf("/") > 0) {

                    int index = temp.lastIndexOf("/");
                    temp = temp.substring(index + 1);
                }
                return temp;
            }
        }
        return filename;
    }

    /**
     * 获取文件后缀（带点）
     * 
     * @param filename
     *            原始文件名称
     * @return
     */
    public static String getFileSuffix(String filename) {
        if (StringUtil.isEmptyString(filename)) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1) {
                return filename.substring(dot);
            } else {
                return "";
            }
        }
        return filename;
    }

    /**
     * 创建目录
     * 
     * @param path
     *            文件相对目录
     */
    public static void crateDir(HttpServletRequest request, String path) {

        File dir = new File(getRealPath(request, path));
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static boolean hasChild(HttpServletRequest request, String path) {

        File dir = new File(getRealPath(request, path));
        return dir.exists() && dir.isDirectory() && isNotEmpty(dir.listFiles());
    }

    public static boolean isNotEmpty(File[] files) {
        for (int index = 0; index < files.length; index++) {
            if (files[index].exists())
                return true;
        }
        return false;
    }

    public static void deleteFile(HttpServletRequest request, String path) {

        File dir = new File(getRealPath(request, path));
        if (dir.exists()) {

            dir.delete();
        }
    }

    /**
     * 绝对路径删除文件
     * 
     * @param realpath
     */
    public static void deleteFile(String realpath) {

        File file = new File(realpath);
        if (file.exists()) {

            try {

                file.delete();

            } catch (Exception e) {

                file.deleteOnExit();
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件，可以是单个文件或文件夹
     * 
     * @param fileName
     *            待删除的文件名
     * @return 文件删除成功返回true,否则返回false
     */
    public static boolean delete(String fileName) throws Exception {
        File file = new File(fileName);
        if (!file.exists()) {
            // System.out.println("删除文件失败："+fileName+"文件不存在");
            return false;
        } else {
            if (file.isFile()) {

                return deleteOneFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     * 
     * @param fileName
     *            被删除文件的文件名
     * @return 单个文件删除成功返回true,否则返回false
     */
    public static boolean deleteOneFile(String fileName) throws Exception {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
            // System.out.println("删除单个文件"+fileName+"成功！");
            return true;
        } else {
            // System.out.println("删除单个文件"+fileName+"失败！");
            return false;
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     * 
     * @param dir
     *            被删除目录的文件路径
     * @return 目录删除成功返回true,否则返回false
     */
    public static boolean deleteDirectory(String dir) throws Exception {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            // System.out.println("删除目录失败"+dir+"目录不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteOneFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            // System.out.println("删除目录失败");
            return false;
        }

        // 删除当前目录
        if (dirFile.delete()) {
            // System.out.println("删除目录"+dir+"成功！");
            return true;
        } else {
            // System.out.println("删除目录"+dir+"失败！");
            return false;
        }
    }

    /**
     * 获取目录真实目录
     * 
     * @param request
     * @param path
     * @return
     */
    public static String getRealPath(HttpServletRequest request, String path) {
        return request.getSession().getServletContext().getRealPath(path);
    }

    public static void downLoad(HttpServletRequest request, HttpServletResponse response, String path,
            String fileName) throws Exception {

        if (StringUtil.isEmptyString(path)) {

            String realPath = getRealPath(request, path);

            File download = new File(realPath);

            if (download.exists()) {

                if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {

                    fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");// firefox浏览器

                } else {

                    fileName = URLEncoder.encode(fileName, "UTF-8");// IE浏览器
                }

                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                response.setHeader("Connection", "close");
                ServletOutputStream sos = response.getOutputStream();
                InputStream inp = new FileInputStream(download);
                byte b[] = new byte[1024];
                int n;
                while ((n = inp.read(b)) != -1) {

                    sos.write(b, 0, n);
                }

                inp.close();
                sos.flush();
                sos.close();
            }
        }
    }

    /**
     * 获取文件大小，单位字节Kb
     * 
     * @param filepath
     * @return
     */
    public static double getFileSize(String filepath) throws Exception {

        File file = new File(filepath);

        if (file.exists()) {

            return getFileSize(file);
        }
        return 0;
    }

    /**
     * 获取文件大小，单位字节Kb
     * 
     * @param file
     * @return
     */
    public static double getFileSize(File file) throws Exception {

        if (file.exists()) {

            FileInputStream inp = new FileInputStream(file);
            double size = (inp.available() / 1024);
            inp.close();
            return size;
        }
        return 0;
    }

    public static String getRootPath(String classPath) {
        String rootPath = "";
        // windows下
        if ("\\".equals(File.separator)) {
            rootPath = classPath.replace("/", "\\");
        }
        // Linux下
        if ("/".equals(File.separator)) {
            rootPath = classPath.replace("\\", "/") + "/";
        }
        return rootPath;
    }

    public static void writerFile(String filePath, String fileName, byte[] xmlContent) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(filePath + File.separator + fileName + ".txt");
        fos.write(xmlContent);
        fos.flush();
        fos.close();
    }

    public static void writerFile(String fileName, byte[] xmlContent) throws IOException {
        // String path =
        // FileUtil.class.getClassLoader().getResource("").getPath();
        String fileItem = "logs" + File.separator + DatetimeUtil.getYear(new Date()) + File.separator
                + DatetimeUtil.getMonth(new Date()) + File.separator + DatetimeUtil.getDay(new Date());
        // String filePath = path.substring(0,
        // path.indexOf("WEB-INF/classes/"))+fileItem;
        writerFile(fileItem, fileName, xmlContent);
    }

    /**
     * 字符流输出
     * 
     * @param message
     * @param liu
     */
    public static void streamWrite(String message, String liu) {
        long t1 = System.currentTimeMillis(); // 排序前取得当前时间
        Calendar c = Calendar.getInstance();

        BufferedOutputStream stream = null;
        FileOutputStream fstream = null;
        try {
            liu.replaceAll(" ", "+");
            byte[] b = Base64.decode(liu);
            fstream = new FileOutputStream(message);

            stream = new BufferedOutputStream(fstream);
            stream.write(b);
            stream.flush();
            long t2 = System.currentTimeMillis(); // 排序后取得当前时间

            c.setTimeInMillis(t2 - t1);

            logger.info("耗时: " + c.get(Calendar.MINUTE) + "分 " + c.get(Calendar.SECOND) + "秒 "
                    + c.get(Calendar.MILLISECOND) + " 毫秒");
            stream.close();
            fstream.close();
        } catch (Exception e) {
            try {
                if (stream != null)
                    stream.close();
                if (fstream != null)
                    fstream.close();
            } catch (Exception ex) {
                logger.error(e.getMessage());
                ex.printStackTrace();
            }
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 字符流输出
     * 
     * @param message
     * @param liu
     */
    public static void streamWrite(String message, InputStream is) {
        long t1 = System.currentTimeMillis(); // 排序前取得当前时间
        Calendar c = Calendar.getInstance();

        BufferedOutputStream stream = null;
        BufferedInputStream bistream = new BufferedInputStream(is);
        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(message);

            stream = new BufferedOutputStream(fstream);
            int len = -1;
            while ((len = bistream.read()) != -1) {
                stream.write(len);
                stream.flush();
            }
            stream.close();
            fstream.close();
            bistream.close();

            long t2 = System.currentTimeMillis(); // 排序后取得当前时间

            c.setTimeInMillis(t2 - t1);

            logger.info("耗时: " + c.get(Calendar.MINUTE) + "分 " + c.get(Calendar.SECOND) + "秒 "
                    + c.get(Calendar.MILLISECOND) + " 毫秒");
        } catch (Exception e) {
            try {
                if (stream != null)
                    stream.close();
                if (fstream != null)
                    fstream.close();
                if (bistream != null)
                    bistream.close();
            } catch (Exception ex) {
                logger.error(e.getMessage());
                ex.printStackTrace();
            }
            logger.error(e.getMessage());
            e.printStackTrace();

        }
    }

    /**
     * String转Stream型方法
     * 
     * @param strFile
     * @return
     */
    public static InputStream string2InputStream(String strFile) {
        ByteArrayInputStream stream = new ByteArrayInputStream(strFile.getBytes());
        return stream;
    }

    /**
     * Stream转String型方法1
     * 
     * @param is
     * @return
     * @throws IOException
     */
    public static String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    /**
     * Stream转String型方法2
     * 
     * @param is
     * @return
     */
    public static String inputStream2String2(InputStream is) {
        String allContent = null;
        try {
            allContent = new String();

            InputStream ins = is;

            ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
            byte[] str_b = new byte[1024];
            int i = -1;
            while ((i = ins.read(str_b)) > 0) {
                outputstream.write(str_b, 0, i);
            }
            allContent = outputstream.toString();
        } catch (Exception e) {

            e.printStackTrace();
        }

        return allContent;
    }

    /**
     * stream转file
     * 
     * @param is
     * @param file
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void inputStream2File(InputStream is, File file) throws IOException, FileNotFoundException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        is.close();
    }

    public static final InputStream byte2Input(byte[] buf) {
        return new ByteArrayInputStream(buf);
    }

    public static final byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    /**
     * 
     * @return 返回资源的二进制数据 @
     */
    public static byte[] readInputStream(InputStream inputStream) {

        // 定义一个输出流向内存输出数据
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 定义一个缓冲区
        byte[] buffer = new byte[1024];
        // 读取数据长度
        int len = 0;
        // 当取得完数据后会返回一个-1
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                // 把缓冲区的数据 写到输出流里面
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        // 得到数据后返回
        return byteArrayOutputStream.toByteArray();

    }

    /**
     * 传入原图名称，，获得一个以时间格式的新名称
     * 
     * @param fileName
     *            　原图名称
     * @return
     */
    public static String generateFileName(String fileName) {
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String formatDate = format.format(new Date());
        int random = new Random().nextInt(10000);
        int position = fileName.lastIndexOf(".");
        String extension = fileName.substring(position);
        return formatDate + random + extension;
    }

    /**
     * 文件流base64编码
     * 
     * @param Value
     * @return
     */
    public static String EncodeBase64(byte[] value) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();

        byte[] d = new byte[4];
        try {
            int count = 0;

            // byte[] x = Value.getBytes(this.Charset);
            // byte[] x = file2byte("");
            byte[] x = value;
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
                    o.write(BASE64_STR.charAt(d[n]));
            }
        } catch (StringIndexOutOfBoundsException e) {
            logger.error(e.toString());
        }
        return o.toString();
    }

    /**
     * 文件流base64解码。
     * 
     * @param value
     * @return
     */
    public String DecodeBase64(byte[] value) {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        String m = "";

        byte[] d = new byte[4];
        try {
            int count = 0;
            // byte[] x = Value.getBytes();
            byte[] x = value;
            while (count < x.length) {
                for (int n = 0; n <= 3; n++) {
                    if (count >= x.length) {
                        d[n] = 64;
                    } else {
                        int y = BASE64_STR.indexOf(x[count]);
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
            logger.error(e.toString());
        }

        try {
            m = o.toString(CHAR_SET);
        } catch (UnsupportedEncodingException ea) {
            logger.error(ea.toString());
        }

        return m;
    }

    /**
     * 获取文件流
     * 
     * @param filePath
     * @return
     */
    public static byte[] file2byte(String filePath) {
        byte[] btemp = null;
        // File f = new File("d:/ac.jpg");
        File f = new File(filePath);
        if (!f.exists()) {
            logger.info("文件存在");
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

    public static boolean deleteDir(String path) {
        if (!(path.endsWith("/") || path.endsWith("\\"))) {
            path += "/";
        }
        File dir = new File(path);
        String[] list = dir.list();
        if (list == null) {
            return true;
        }
        for (int i = 0; i < list.length; i++) {
            File f = new File(path + list[i]);
            if (f.isFile()) {
                if (f.delete()) {
                    logger.info("file deleted: " + path + list[i]);
                } else {
                    logger.error("cannot delete file: " + path + list[i]);
                    return false;
                }
            } else {
                if (!deleteDir(path + list[i] + "/")) {
                    logger.error("cannot delete directory: " + path + list[i] + "/");
                    return false;
                }
            }
        }
        if (delete(dir)) {
            return true;
        } else {
            logger.error("cannot delete directory:" + path);
            return false;
        }
    }

    private static boolean delete(File file) {
        for (int i = 0; i < 60; i++) {
            if (file.delete()) {
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e, e);
            }
        }
        return false;
    }

    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static void makeDir(String path) {
        (new File(path)).mkdir();
    }

    public static List<File> fileList(String path, boolean recursiveFlag) {
        List<File> fileLists = new ArrayList<File>();
        if (!(path.endsWith("/") || path.endsWith("\\"))) {
            path += "/";
        }
        File dir = new File(path);
        String[] list = dir.list();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                File f = new File(path + list[i]);
                if (f.isFile()) {
                    fileLists.add(f);
                } else {
                    if (recursiveFlag) {
                        List<File> child_files = fileList(f.getPath(), recursiveFlag);
                        for (int j = 0; j < child_files.size(); j++) {
                            fileLists.add(child_files.get(j));
                        }
                    }
                }
            }
        }
        return fileLists;
    }

    public static List<File> dirList(String path) {
        List<File> fileLists = new ArrayList<File>();
        if (!(path.endsWith("/") || path.endsWith("\\"))) {
            path += "/";
        }
        File dir = new File(path);
        String[] list = dir.list();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                File f = new File(path + list[i]);
                if (!f.isFile()) {
                    fileLists.add(f);
                }
            }
        }
        return fileLists;
    }

    public static String getMD5(String fileName) {
        StringBuilder rtn = new StringBuilder("");
        try {
            DigestInputStream inStream = new DigestInputStream(new BufferedInputStream(new FileInputStream(
                    fileName)), MessageDigest.getInstance("MD5"));
            byte[] buf = new byte[1024];
            for (;;) {
                if (inStream.read(buf) <= 0) {
                    break;
                }
            }
            inStream.close();
            MessageDigest md5 = inStream.getMessageDigest();
            byte[] digest = md5.digest();
            for (int i = 0; i < digest.length; i++) {
                rtn.append(Integer.toString((digest[i] & 0xf0) >> 4, 16));
                rtn.append(Integer.toString(digest[i] & 0x0f, 16));
            }
        } catch (IOException e) {
            logger.error(e, e);
        } catch (NoSuchAlgorithmException e) {
            logger.error(e, e);
        }
        return rtn.toString();
    }
}
