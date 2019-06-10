package com.ysttench.application.common.util;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocument;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlParser {
    
    /**
     * 将map转为XML字符串
     * @param map
     * @return
     */
    public static Document map2XML(Map<String, Object> map, String uri) {
        Document doc = new DOMDocument();
        doc.setXMLEncoding("UTF-8");
        Element root = doc.addElement("TranData");
        Element header = root.addElement("Head");
        Element interNo = header.addElement("InterNo");
        interNo.setText(uri);
        Element body = root.addElement("BODY");
        map2XML(map, body);

        return doc;
    }
    
    /**
     * 将map对象的数据转为BODY节点下的XML格式数据
     * @param map
     * @param body
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void map2XML(Map<String, Object> map, Element body){
        Iterator<String> it = map.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            Object value = map.get(key); 
            Element column = body.addElement(key);
            if(value instanceof List){
                list2XML((List)value, column);
            }else if(value instanceof Map){
                map2XML((Map)value, column);
            }else{
                column.setText(value==null ? null : value.toString());
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void list2XML(List list, Element e){
        for(Object item : list){
            if(item instanceof Map){
                map2XML((Map<String, Object>)item, e);
            }
        }
    }

    /**
     * 将XML字符串转换为Document对象
     * @param xml
     * @return
     */
    public static Document parseDocument(String xml) throws Exception {
        StringReader sr = new StringReader(xml);
        SAXReader saxReader = new SAXReader();
        return saxReader.read(sr);
    }
    
    /**
     * 将XML字符串转换为Document对象
     * @param xml
     * @return
     */
    public static Document parseDocument(byte[] data,String encoding) throws Exception {
        String xml = new String(data,encoding);
        return parseDocument(xml);
    }
    
    /**
     * 创建document head
     * @param serviceId 服务id
     * @return
     */
    public static Document createDocHead(String serviceId){
        Document doc = new DOMDocument();
        doc.setXMLEncoding("GBK");
        Element root = doc.addElement("TranData");
        Element head = root.addElement("Head");
        Element tranDate = head.addElement("TranDate");
        tranDate.setText(DatetimeUtil.getDateyyyyMMdd());
        Element tranTime = head.addElement("TranTime");
        tranTime.setText(DatetimeUtil.getDateHHmmss());
        //交易流水号
        Element tranNoEle = head.addElement("TranNo");
        String tranNo = serviceId+ DatetimeUtil.getDateyyyyMMddhhmmss()+System.currentTimeMillis();
        tranNoEle.setText(tranNo);
        Element serviceIdEle = head.addElement("ServiceId");
        serviceIdEle.setText(serviceId);
        //请求头添加节点，区分来源（代理人或微信）  add by jinweitao  2016-2-24 17:52:55
        Element sourceEle = head.addElement("Source");
        sourceEle.setText("01");
        return doc;
    }
    
    /**
     * 将Document以指定的编码方式输出。如果不指定编码方式，缺省的编码方式是GBK
     * 
     * @param doc
     *            Document
     * @param strEncoding
     *            String
     * @return InputStream
     */
    public static byte[] getBytes(Document doc, String strEncoding) {
        String strEnc = strEncoding;
        if (strEnc == null || strEnc.equals("")) {
            strEnc = "GBK";
        }
        try {
            // 输出格式化器
            OutputFormat format = new OutputFormat("  ", true);
            // 设置编码
            format.setEncoding(strEnc);
            // xml输出器
            StringWriter out = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(out, format);
            // 打印doc
            xmlWriter.write(doc);
            xmlWriter.flush();
            // 关闭输出器的流，即是printWriter
            String xml = out.toString();
            return xml.getBytes(strEnc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 将Document以指定的编码方式输出。如果不指定编码方式，缺省的编码方式是GBK
     * 
     * @param doc
     *            Document
     * @param strEncoding
     *            String
     * @return InputStream
     */
    public static byte[] getBytes(org.jdom2.Document doc, String strEncoding) {
        String strEnc = strEncoding;

        if (strEnc == null || strEnc.equals("")) {
            strEnc = "GBK";
        }
        XMLOutputter outputter = null;
        try {
            Format format = Format.getPrettyFormat();
            format.setEncoding(strEnc);
            format.setIndent("  ");
            format.setExpandEmptyElements(true);
            outputter = new XMLOutputter(format);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            outputter.output(doc, baos);
            baos.close();

            return baos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    
    /**
     * 生成返回报文
     * @param flag  交易结果 0：成功，1：失败
     * @param desc 交易结果描述
     * @return
     */
    public static Document createOutXmlDoc(String flag,String desc){
        Document outXmlDoc = new DOMDocument();
        Element tTranEle = outXmlDoc.addElement("TranData");
        Element tHeadEle = tTranEle.addElement("Head");
        tTranEle.addElement("Body");
        Element tFlagEle = tHeadEle.addElement("Flag");
        Element tDescEle = tHeadEle.addElement("Desc");
        tFlagEle.setText(flag);
        tDescEle.setText(desc);
        return outXmlDoc;
    }
}
