package com.ysttench.application.common.util.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadImage {

    /**
     * 通过file 对象将图片转换成流
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public static byte[] getImageStream(File file) throws Exception {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                file));
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

        byte[] temp = new byte[1024];
        int size = 0;
        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        in.close();
        byte[] content = out.toByteArray();
        return content;
    }

    /**
     * 读取微信图片
     * 
     * @param imageUrl
     *            　图片URL
     * @param filePath
     *            图片写入的路径
     * @param fileName
     *            文件名字
     * @throws Exception
     */
    public static byte[] readInputStream(String picUrl) throws Exception {
        // new一个URL对象
        URL url = new URL(picUrl);
        // 设置代理链接
//
//      Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
//              "10.100.61.90", 8080));

        // 打开链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置请求方式为"GET"
        conn.setRequestMethod("GET");
        // 超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        // 通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 得到图片的二进制数据，以二进制封装得到数据
        return outStream.toByteArray();
        // // new一个文件对象用来保存图片,保存到用户指定的目录
        // File imageFile = new File(filePath + "/");
        // if (!imageFile.exists()) {
        // imageFile.mkdirs();
        // }
        // imageFile = new File(filePath + "/", fileName);
        // // 创建输出流
        // FileOutputStream fileOut = new FileOutputStream(imageFile);
        // // 写入数据
        // fileOut.write(data);
        // // 关闭输出流
        // fileOut.close();
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // byte[] data =
        // readInputStream("http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=XfqTUUqYYe2m-3qDmyisIoVsPCqzUUGy91wK7_1u_EI2oslG2PAcPMf8NrH_3YCmvjIgN79ErPZ4Nypf36T8FfsDmBUC451N_TXIGgi3fv53gnfvq7GchSn0muS-OT6XeNj0mzLkuJnvKHxG2289xw&media_id=njQOy2JhB7Za0MnA1YdJjJ0xNNrQqPhLpIutJn-NieWxVeD0bH0MKyfLzBnXXuW2");
        // FileOutputStream fos = new FileOutputStream(new
        // File("c:\\test.jpg"));
        // fos.write(data);
        // fos.flush();
        // fos.close();
    }
}
