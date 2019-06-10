package com.ysttench.application.common.util.file;

/** 
 *  缩略图实现，将图片(jpg、bmp、png、gif等等)真实的变成想要的大小 
 */
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

/*******************************************************************************
 * 缩略图类（通用） 本java类能将jpg、bmp、png、gif图片文件，进行等比或非等比的大小转换。 具体使用方法
 * compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
 */
public class CompressPic {
    private static Log log = LogFactory.getLog(CompressPic.class);
    private File file = null; // 文件对象
    private String inputDir; // 输入图路径
    private String outputDir; // 输出图路径
    private String inputFileName; // 输入图文件名
    private String outputFileName; // 输出图文件名
    private int outputWidth = 100; // 默认输出图片宽
    private int outputHeight = 100; // 默认输出图片高
    private boolean proportion = true; // 是否等比缩放标记(默认为等比缩放)

    public CompressPic() { // 初始化变量
        inputDir = "";
        outputDir = "";
        inputFileName = "";
        outputFileName = "";
        outputWidth = 100;
        outputHeight = 100;
    }

    public void setInputDir(String inputDir) {
        this.inputDir = inputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public void setOutputWidth(int outputWidth) {
        this.outputWidth = outputWidth;
    }

    public void setOutputHeight(int outputHeight) {
        this.outputHeight = outputHeight;
    }

    public void setWidthAndHeight(int width, int height) {
        this.outputWidth = width;
        this.outputHeight = height;
    }

    /*
     * 获得图片大小 传入参数 String path ：图片路径
     */
    public long getPicSize(String path) {
        file = new File(path);
        return file.length();
    }

    // 图片处理
    public String compressPic() {
        try {
            // 获得源文件
            file = new File(inputDir + inputFileName);
            if (!file.exists()) {
                return "";
            }
            Image img = ImageIO.read(file);
            // 判断图片格式是否正确
            if (img.getWidth(null) == -1) {
                log.info(" can't read,retry!" + "<BR>");
                return "no";
            } else {
                int newWidth;
                int newHeight;
                // 判断是否是等比缩放
                if (this.proportion == true) {
                    // 为等比缩放计算输出的图片宽度及高度
                    double rate1 = ((double) img.getWidth(null)) / (double) outputWidth + 0.1;
                    double rate2 = ((double) img.getHeight(null)) / (double) outputHeight + 0.1;
                    // 根据缩放比率大的进行缩放控制
                    double rate = rate1 > rate2 ? rate1 : rate2;
                    newWidth = (int) (((double) img.getWidth(null)) / rate);
                    newHeight = (int) (((double) img.getHeight(null)) / rate);
                } else {
                    newWidth = outputWidth; // 输出的图片宽度
                    newHeight = outputHeight; // 输出的图片高度
                }
                BufferedImage tag = new BufferedImage((int) newWidth, (int) newHeight, BufferedImage.TYPE_INT_RGB);

                /*
                 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
                 */
                tag.getGraphics().drawImage(img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
                // FileOutputStream out = new FileOutputStream(outputDir +
                // outputFileName);
                // // JPEGImageEncoder可适用于其他图片类型的转换
                // JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                // encoder.encode(tag);
                // out.close();
                ImageIO.write(tag, "jpeg", new File(outputDir + outputFileName));
                tag.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "ok";
    }

    public String compressPic(String inputDir, String outputDir, String inputFileName, String outputFileName) {
        // 输入图路径
        this.inputDir = inputDir;
        // 输出图路径
        this.outputDir = outputDir;
        // 输入图文件名
        this.inputFileName = inputFileName;
        // 输出图文件名
        this.outputFileName = outputFileName;
        return compressPic();
    }

    public String compressPic(String inputDir, String outputDir, String inputFileName, String outputFileName,
            int width, int height, boolean gp) {
        // 输入图路径
        this.inputDir = inputDir;
        // 输出图路径
        this.outputDir = outputDir;
        // 输入图文件名
        this.inputFileName = inputFileName;
        // 输出图文件名
        this.outputFileName = outputFileName;
        // 设置图片长宽
        setWidthAndHeight(width, height);
        // 是否是等比缩放 标记
        this.proportion = gp;
        return compressPic();
    }

    /**
     * 微转电图片压缩
     * 
     * @param imageFile
     * @param path
     * @return
     */
    public String compressPic(MultipartFile imageFile, String path, String fileName) {
        try {
            SaveFileFromInputStream(imageFile.getInputStream(), path, fileName);

            BufferedImage sourceImg = ImageIO.read(imageFile.getInputStream());
            setWidthAndHeight(sourceImg.getWidth(), sourceImg.getHeight());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 输入图路径
        this.inputDir = path;
        // 输出图路径
        this.outputDir = path;
        // 输入图文件名
        this.inputFileName = fileName;
        // 输出图文件名
        this.outputFileName = fileName;

        // 是否是等比缩放 标记
        this.proportion = true;
        return compressPic();
    }

    public void SaveFileFromInputStream(InputStream stream, String path, String filename) throws IOException {
        File dirPath = new File(path);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        FileOutputStream fs = new FileOutputStream(path + "/" + filename);
        byte[] buffer = new byte[1024 * 1024];
        // int bytesum = 0;
        int byteread = 0;
        while ((byteread = stream.read(buffer)) != -1) {
            // bytesum+=byteread;
            fs.write(buffer, 0, byteread);
            fs.flush();
        }
        fs.close();
        stream.close();
    }

    public byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    // main测试
    // compressPic(大图片路径,生成小图片路径,大图片文件名,生成小图片文名,生成小图片宽度,生成小图片高度,是否等比缩放(默认为true))
    public static void main(String[] arg) {
        CompressPic mypic = new CompressPic();
        log.info("输入的图片大小：" + mypic.getPicSize("e:\\1.JPG") / 1024 + "KB");
        int count = 0; // 记录全部图片压缩所用时间
        File file = new File("e:\\1.JPG");

        try {
            BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));

            int start = (int) System.currentTimeMillis(); // 开始时间
            mypic.compressPic("e:\\", "e:\\test\\", "1.JPG", "1.JPG", sourceImg.getWidth(), sourceImg.getHeight(), true);
            int end = (int) System.currentTimeMillis(); // 结束时间
            int re = end - start; // 但图片生成处理时间
            count += re;
            log.info("第" + (1) + "张图片压缩处理使用了: " + re + "毫秒");
            log.info("输出的图片大小：" + mypic.getPicSize("e:\\test\\1.JPG") / 1024 + "KB");

            log.info("总共用了：" + count + "毫秒");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}