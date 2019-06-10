package com.ysttench.application.common.util.excel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.NotImplementedException;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.converter.core.BasicURIResolver;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import com.ysttench.application.common.exception.SystemException;


/**
 * 读写EXCEL文件
 */
public class POIUtils {
    private static Logger log = LoggerFactory.getLogger(POIUtils.class);
    /**
     * 存放图片的路径
     */
    private final static String IMAGE_PATH = "image";

    /**
     * @author lijianning Email：mmm333zzz520@163.com date：2015-11-11
     * @param exportData
     *            列表头
     * @param lis
     *            数据集
     * @param fileName
     *            文件名
     * 
     */
    public static void exportToExcel(HttpServletResponse response, List<Map<String, Object>> exportData,
            List<?> lis, String fileName) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ExcelUtil.createWorkBook(exportData, lis).write(os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((fileName + ".xls").getBytes("utf-8"), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
            }

        }
    }
    
    public static void docToHtml(String sourceFileName, String targetFileName) {
        initFolder(targetFileName);
        String imagePathStr = initImageFolder(targetFileName);
        try {
            HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(sourceFileName));
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
            HtmlPicturesManager picturesManager = new HtmlPicturesManager(imagePathStr,IMAGE_PATH);
            wordToHtmlConverter.setPicturesManager(picturesManager);
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();
            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(new File(targetFileName));

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
        } catch (Exception e) {
            log.error("将doc文件转换为html时出错", e);
            throw new SystemException("将doc文件转换为html时出错!" + e.getMessage());
        }
    }

    public static void docxToHtml(String sourceFileName, String targetFileName) {
        initFolder(targetFileName);
        String imagePathStr = initImageFolder(targetFileName);
        OutputStreamWriter outputStreamWriter = null;
        try {
            XWPFDocument document = new XWPFDocument(new FileInputStream(sourceFileName));
            XHTMLOptions options = XHTMLOptions.create();
            //存放图片的文件夹
            options.setExtractor( new FileImageExtractor( new File(imagePathStr ) ) );
            //html中图片的路径
            options.URIResolver( new BasicURIResolver(IMAGE_PATH));
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(targetFileName),"utf-8");
            XHTMLConverter xhtmlConverter = (XHTMLConverter)XHTMLConverter.getInstance();
            xhtmlConverter.convert(document, outputStreamWriter, options);
        } catch (Exception e) {
            log.error("将docx文件转换为html时出错", e);
            throw new SystemException("将docx文件转换为html时出错!" + e.getMessage());
        }finally{
            try{
                if(outputStreamWriter!=null){
                    outputStreamWriter.close();
                }
            }catch(Exception e){
                log.error("关闭文件流时出错", e);
            }
        }
    }

    public static void xlsToHtml(String sourceFileName, String targetFileName) {
        initFolder(targetFileName);
        try {
            Document doc = ExcelToHtmlConverter.process( new File( sourceFileName) );
            DOMSource domSource = new DOMSource( doc );
            StreamResult streamResult = new StreamResult( new File(targetFileName) );
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer serializer = tf.newTransformer();
            serializer.setOutputProperty( OutputKeys.ENCODING, "UTF-8" );
            serializer.setOutputProperty( OutputKeys.INDENT, "yes" );
            serializer.setOutputProperty( OutputKeys.METHOD, "html" );
            serializer.transform( domSource, streamResult );
        }catch (Exception e) {
            log.error("将xls文件转换为html时出错", e);
            throw new SystemException("将xls文件转换为html时出错!" + e.getMessage());
        }
    }

    public static void xlsxToHtml(String sourceFileName, String targetFileName) {
        throw new NotImplementedException();
    }

    /**
     * 初始化存放html文件的文件夹
     * @param targetFileName html文件的文件名
     */
    private static void initFolder (String targetFileName){
        File targetFile = new File(targetFileName);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        String targetPathStr = targetFileName.substring(0, targetFileName.lastIndexOf(File.separator));
        File targetPath = new File(targetPathStr);
        //如果文件夹不存在，则创建
        if (!targetPath.exists()) {
            targetPath.mkdirs();
        }
    }

    /**
     * 初始化存放图片的文件夹
     * @param htmlFileName html文件的文件名
     * @return 存放图片的文件夹路径
     */
    private static String initImageFolder(String htmlFileName){
        String targetPathStr = htmlFileName.substring(0, htmlFileName.lastIndexOf(File.separator));
        //创建存放图片的文件夹
        String imagePathStr = targetPathStr + File.separator + IMAGE_PATH+ File.separator;
        File imagePath = new File(imagePathStr);
        if (imagePath.exists()) {
            imagePath.delete();
        }
        imagePath.mkdir();
        return imagePathStr;
    }
    
    /**
     * 读取doc文件的文本，不带格式
     * @param fileName 文件名
     * @return 文件的文本内容
     */
    public static String readTextForDoc(String fileName){
        String text;
        try(
                FileInputStream in = new FileInputStream(fileName);
                WordExtractor wordExtractor = new WordExtractor(in);
        ){
            text = wordExtractor.getText();
        }catch(Exception e){
            log.error("读取文件出错:"+fileName, e);
            throw new SystemException("读取文件出错",e);
        }
        return text;
    }

    /**
     * 读取docx文件的内容，只读取纯文本内容，不带格式
     * @param fileName 文件名
     * @return 文件内容
     */
    public static String readTextForDocx(String fileName) {
        String text = null;
        POIXMLTextExtractor ex = null;
        try{
            OPCPackage oPCPackage = POIXMLDocument.openPackage(fileName);
            XWPFDocument xwpf = new XWPFDocument(oPCPackage);
            ex = new XWPFWordExtractor(xwpf);
            text = ex.getText();
        }catch(Exception e){
            log.error("读取文件出错,文件名:"+fileName, e);
            throw new SystemException("读取文件出错",e);
        }finally{
            try{
                if(ex!=null){
                    ex.close();
                }
            }catch(Exception e){
                log.error("关闭流出错:"+fileName, e);
            }
        }
        return text;
    }
}