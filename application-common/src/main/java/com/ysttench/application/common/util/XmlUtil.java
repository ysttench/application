package com.ysttench.application.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.thoughtworks.xstream.XStream;
import com.ysttench.application.common.server.HttpClientUtil;

/**
 * xml工具类
 * @author miklchen
 *
 */
public class XmlUtil {

    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     * @param strxml
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> doXMLParse(String strxml) throws JDOMException, IOException {
        if(null == strxml || "".equals(strxml)) {
            return null;
        }
        
        Map<String, String> m = new HashMap<String, String>();
        InputStream in = HttpClientUtil.String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if(children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = XmlUtil.getChildrenText(children);
            }
            
            m.put(k, v);
        }
        
        //关闭流
        in.close();
        
        return m;
    }
    
    /**
     * 获取子结点的xml
     * @param children
     * @return String
     */
    @SuppressWarnings({ "rawtypes" })
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if(!children.isEmpty()) {
            Iterator it = children.iterator();
            while(it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if(!list.isEmpty()) {
                    sb.append(XmlUtil.getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 获取xml编码字符集
     * @param strxml
     * @return
     * @throws IOException 
     * @throws JDOMException 
     */
    public static String getXMLEncoding(String strxml) throws JDOMException, IOException {
        InputStream in = HttpClientUtil.String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        in.close();
        return (String)doc.getProperty("encoding");
    }
    
    /**
     * 将Bean转换为XML
     *
     * @param clazzMap 别名-类名映射Map
     * @param bean     要转换为xml的bean对象
     * @return XML字符串
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String bean2xml(Map<String, Class> clazzMap, Object bean) {
        XStream xstream = new XStream();
        for (Iterator it = clazzMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Class> m = (Map.Entry<String, Class>) it.next();
            xstream.alias(m.getKey(), m.getValue());
        }
        String xml = xstream.toXML(bean);
        return xml;
    }

    /**
     * 将XML转换为Bean
     *
     * @param clazzMap 别名-类名映射Map
     * @param xml      要转换为bean对象的xml字符串
     * @return Java Bean对象
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object xml2Bean(Map<String, Class> clazzMap, String xml) {
        XStream xstream = new XStream();
        for (Iterator it = clazzMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Class> m = (Map.Entry<String, Class>) it.next();
            xstream.alias(m.getKey(), m.getValue());
        }
        Object bean = xstream.fromXML(xml);
        return bean;
    }

    /**
     * 获取XStream对象
     *
     * @param clazzMap 别名-类名映射Map
     * @return XStream对象
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static XStream getXStreamObject(Map<String, Class> clazzMap) {
        XStream xstream = new XStream();
        for (Iterator it = clazzMap.entrySet().iterator(); it.hasNext();) {
            Map.Entry<String, Class> m = (Map.Entry<String, Class>) it.next();
            xstream.alias(m.getKey(), m.getValue());
        }
        return xstream;
    }
}
