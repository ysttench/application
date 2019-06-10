package com.ysttench.application.common.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSON;

public class JsonUtils {
    private static Log log = LogFactory.getLog(JsonUtils.class);
    
    /**
     * JSON 对象转换（fastjson）
     * @param json
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static<T> T parse(String json, Class<T> clazz){
        try {
            if(Map.class.isAssignableFrom(clazz)){
                return (T)JSON.parse(json);
            }
            T target = clazz.newInstance();
            com.alibaba.fastjson.JSONObject jsonO = (com.alibaba.fastjson.JSONObject)JSON.parse(json);
            Iterator<String> it = jsonO.keySet().iterator();
            while(it.hasNext()){
                String key = it.next();
                Object value= jsonO.get(key);
                if(value instanceof com.alibaba.fastjson.JSONArray){
                    
                    // 命名不规范，首字母大写转成小写。
                    key = StringUtil.toLowerCaseFirstOne(key);
                    Class<?> typeClazz = BeanUtil.getPropertyType(target, key);
                    if (typeClazz == null) continue;
                    String jsonValue = ((com.alibaba.fastjson.JSONArray)value).toJSONString();
                    if(typeClazz.equals(List.class)){
                        Field field = BeanUtil.getField(target, key);
                        Type[] types = (Type[])BeanUtil.getPropertyValue(field.getGenericType(), "actualTypeArguments");
                        typeClazz = (Class<?>)types[0];
                        value = JSON.parseArray(jsonValue, typeClazz);
                    } else {
                        value = JSON.parseArray(jsonValue, typeClazz.getComponentType());
                    }
                }else if(value instanceof com.alibaba.fastjson.JSONObject){
                    Class<?> typeClazz = BeanUtil.getPropertyType(target, key);
                    if (typeClazz == null) continue;
                    String jsonValue = ((com.alibaba.fastjson.JSONObject)value).toJSONString();
                    value = parse(jsonValue, typeClazz);
                }
                BeanUtil.setPropertyValue(target, key, value);
            }
            return target;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
//            throw new BusinessException(ResultMsg.BEAN_NEW_INSTANCE_EXCEPTION);
        } 
        return null;
    }
    
    public static String object2json(Object obj) {
        StringBuilder json = new StringBuilder();
        try {
            if (obj == null) {
                json.append("\"\"");
            } else if (obj instanceof String || obj instanceof Integer
                    || obj instanceof Float || obj instanceof Boolean
                    || obj instanceof Short || obj instanceof Double
                    || obj instanceof Long || obj instanceof BigDecimal
                    || obj instanceof BigInteger || obj instanceof Byte
                    /*|| obj instanceof Date*/) {
                json.append("\"").append(string2json(obj.toString())).append("\"");
            } else if (obj instanceof Object[]) {
                json.append(array2json((Object[]) obj));
            } else if (obj instanceof List) {
                json.append(list2json((List<?>) obj));
            } else if (obj instanceof Map) {
                json.append(map2json((Map<?, ?>) obj));
            } else if (obj instanceof Set) {
                json.append(set2json((Set<?>) obj));
            } else if (obj instanceof Date) {
                json.append("\"").append(string2json(DatetimeUtil.dateToStrLong((Date)obj))).append("\"");
            } else {
                json.append(bean2json(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    public static String bean2json(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        PropertyDescriptor[] props = null;
        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
        } catch (IntrospectionException e) {
        }
        if (!(props == null || props.length == 0)) {
            for (int i = 0; i < props.length; i++) {
                try {
                    String name = object2json(props[i].getName());
                    String value = object2json(props[i].getReadMethod().invoke(bean));
                    json.append(name);
                    json.append(":");
                    json.append(value);
                    json.append(",");
                } catch (Exception e) {
                }
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }

    public static String list2json(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static String array2json(Object[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (Object obj : array) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static String map2json(Map<?, ?> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        if (map != null && map.size() > 0) {
            for (Object key : map.keySet()) {
                json.append(object2json(key));
                json.append(":");
                json.append(object2json(map.get(key)));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }

    public static String set2json(Set<?> set) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (set != null && set.size() > 0) {
            for (Object obj : set) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }

    public static String string2json(String s) {
        if (s == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
            case '\'':
                sb.append("\\\'");
                break;
            case '"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '/':
                sb.append("\\/");
                break;
            default:
                if (ch >= '\u0000' && ch <= '\u001F') {
                    String ss = Integer.toHexString(ch);
                    sb.append("\\u");
                    for (int k = 0; k < 4 - ss.length(); k++) {
                        sb.append('0');
                    }
                    sb.append(ss.toUpperCase());
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> parseJSON2List(String jsonStr) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            JSONArray jsonArr = JSONArray.fromObject(jsonStr);
            Iterator<JSONObject> it = jsonArr.iterator();
            while (it.hasNext()) {
                JSONObject json2 = it.next();
                list.add(parseJSON2Map(json2.toString()));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseJSON2Map(String jsonStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 最外层解析
        try {
            JSONObject json = JSONObject.fromObject(jsonStr);
            for (Object k : json.keySet()) {
                Object v = json.get(k);
                // 如果内层还是数组的话，继续解析
                if (v instanceof JSONArray) {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    Iterator<JSONObject> it = ((JSONArray) v).iterator();
                    while (it.hasNext()) {
                        JSONObject json2 = it.next();
                        list.add(parseJSON2Map(json2.toString()));
                    }
                    map.put(k.toString(), list);
                } else {
                    map.put(k.toString(), v);
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static Object jsonToObject(String json, Class clazz) {
        JSONObject obj = JSONObject.fromObject(json);
        return JSONObject.toBean(obj, clazz);
    }

    @SuppressWarnings("rawtypes")
    public static Object jsonToObject(String json, Class clazz, Map map) {
        JSONObject obj = JSONObject.fromObject(json);
        if(map == null || map.isEmpty()){
            return JSONObject.toBean(obj, clazz);
        }
        return JSONObject.toBean(obj, clazz, map);
    }

    public static String beanToJson1(Object obj, JsonConfig config) {
        JSONObject jsonArray = null;
        if(config == null){
            return beanToJson1(obj);
        } else {
            jsonArray = JSONObject.fromObject(obj, config);
        }
        return jsonArray.toString();
    }
    
    public static String beanToJson1(Object obj) {
        JSONObject jsonObject = JSONObject.fromObject(obj);
        return jsonObject.toString();
    }
//  public static boolean isGoodJson(String json) {  
//        if (StringUtils.isBlank(json)) {  
//            return false;  
//        }  
//        try {  
//            new com.google.gson.JsonParser().parse(json);
//            return true;  
//        } catch (Exception e) {  
//            System.out.println("bad json: " + json);  
//            return false;  
//        }  
//    }

    public static void main(String[] args) {
        String str = "{dayRange:5,changeRateGt:'20',changeRateLt:'-20'}";
        Map<String, Object> jsonMap = parseJSON2Map(str);
        System.out.println(jsonMap);
    }
    /**
     * trans json string to java object
     * 
     * @param json
     * @param o
     * @return
     */
    public static Object getObjectfromJacksonJson(String json, Class<?> clazz) {
        Object obj = null;
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
        if(json == null || json.equals("")){
            return null;
        }
        try {
            obj = mapper.readValue(json, clazz);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * String   @param s
     * String   @return
     */
    public static String twser(String s){
        return null;
    }

    public static String getStringForJson(String str) {
        if (str != null) {
            str = str.replace("\r", "\\r");
            str = str.replace("\n", "\\n");
            str = str.replace("\'", "\\\'");
            str = str.replace("\"", "\\\"");
            return str;
        } else {
            return "";
        }
    }
    
    public static boolean isEmpty(String s) {
        if (s == null) return true;
        
        if ("".equals(s)) return true;
        
        if ("\"\"".equals(s)) return true;
        if ("\"null\"".equals(s)) return true;
        
        return false;
    }
    
    /**
     *  一个String字符串转换为json格式
     *@descript
     *@param s
     *@return
     *@author lijianning
     *@date 2015年6月15日
     *@version 1.0v
     */
    public static String stringToJson(String s) { 
        if (s == null) { 
            return nullToJson(); 
        } 
        StringBuilder sb = new StringBuilder(); 
        for (int i = 0; i < s.length(); i++) { 
            char ch = s.charAt(i); 
            switch (ch) { 
            case '"': 
                sb.append("\\\""); 
                break; 
            case '\\': 
                sb.append("\\\\"); 
                break; 
            case '\b': 
                sb.append("\\b"); 
                break; 
            case '\f': 
                sb.append("\\f"); 
                break; 
            case '\n': 
                sb.append("\\n"); 
                break; 
            case '\r': 
                sb.append("\\r"); 
                break; 
            case '\t': 
                sb.append("\\t"); 
                break; 
            case '/': 
                sb.append("\\/"); 
                break; 
            default: 
                if (ch >= '\u0000' && ch <= '\u001F') { 
                    String ss = Integer.toHexString(ch); 
                    sb.append("\\u"); 
                    for (int k = 0; k < 4 - ss.length(); k++) { 
                        sb.append('0'); 
                    } 
                    sb.append(ss.toUpperCase()); 
                } else { 
                    sb.append(ch); 
                } 
            } 
        } 
        return sb.toString(); 
    } 
   
    public static String nullToJson() { 
        return ""; 
    } 
   
    /**
     * 一个obj对象转换为json格式
     *@descript
     *@param obj
     *@return
     *@author lijianning
     *@date 2015年6月15日
     *@version 1.0v
     */
    public static String objectToJson(Object obj) { 
        StringBuilder json = new StringBuilder(); 
        if (obj == null) { 
            json.append("\"\""); 
        } else if (obj instanceof Number) { 
            json.append(numberToJson((Number) obj)); 
        } else if (obj instanceof Boolean) { 
            json.append(booleanToJson((Boolean) obj)); 
        } else if (obj instanceof String) { 
            json.append("\"").append(stringToJson(obj.toString())).append("\""); 
        } else if (obj instanceof Object[]) { 
            json.append(arrayToJson((Object[]) obj)); 
        } else if (obj instanceof List) { 
            json.append(listToJson((List<?>) obj)); 
        } else if (obj instanceof Map) { 
            json.append(mapToJson((Map<?, ?>) obj)); 
        } else if (obj instanceof Set) { 
            json.append(setToJson((Set<?>) obj)); 
        } else { 
            json.append(beanToJson(obj)); 
        } 
        return json.toString(); 
    } 
   
    public static String numberToJson(Number number) { 
        return number.toString(); 
    } 
   
    public static String booleanToJson(Boolean bool) { 
        return bool.toString(); 
    } 
   
   /**
    *  一个bean对象转换为json格式
    *@descript
    *@param bean
    *@return
    *@author lijianning
    *@date 2015年6月15日
    *@version 1.0v
    */
    public static String beanToJson(Object bean) { 
        StringBuilder json = new StringBuilder(); 
        json.append("{"); 
        PropertyDescriptor[] props = null; 
        try { 
            props = Introspector.getBeanInfo(bean.getClass(), Object.class) 
                    .getPropertyDescriptors(); 
        } catch (IntrospectionException e) { 
        } 
        if (props != null) { 
            for (int i = 0; i < props.length; i++) { 
                try { 
                    String name = objectToJson(props[i].getName()); 
                    String value = objectToJson(props[i].getReadMethod() 
                            .invoke(bean)); 
                    json.append(name); 
                    json.append(":"); 
                    json.append(value); 
                    json.append(","); 
                } catch (Exception e) { 
                } 
            } 
            json.setCharAt(json.length() - 1, '}'); 
        } else { 
            json.append("}"); 
        } 
        return json.toString(); 
    } 
   
    /**

     *@descript
     *@param list
     *@return
     *@author lijianning
     *@date 2015年6月15日
     *@version 1.0v
     */
    public static String listToJson(List<?> list) { 
        StringBuilder json = new StringBuilder(); 
        json.append("["); 
        if (list != null && list.size() > 0) { 
            for (Object obj : list) { 
                json.append(objectToJson(obj)); 
                json.append(","); 
            } 
            json.setCharAt(json.length() - 1, ']'); 
        } else { 
            json.append("]"); 
        } 
        return json.toString(); 
    } 
   
    /**
     *  一个数组集合转换为json格式
     *@descript
     *@param array
     *@return
     *@author lijianning
     *@date 2015年6月15日
     *@version 1.0v
     */
    public static String arrayToJson(Object[] array) { 
        StringBuilder json = new StringBuilder(); 
        json.append("["); 
        if (array != null && array.length > 0) { 
            for (Object obj : array) { 
                json.append(objectToJson(obj)); 
                json.append(","); 
            } 
            json.setCharAt(json.length() - 1, ']'); 
        } else { 
            json.append("]"); 
        } 
        return json.toString(); 
    } 
   
   /**
    * 一个Map集合转换为json格式
    *@descript
    *@param map
    *@return
    *@author lijianning
    *@date 2015年6月15日
    *@version 1.0v
    */
    public static String mapToJson(Map<?, ?> map) { 
        StringBuilder json = new StringBuilder(); 
        json.append("{"); 
        if (map != null && map.size() > 0) { 
            for (Object key : map.keySet()) { 
                json.append(objectToJson(key)); 
                json.append(":"); 
                json.append(objectToJson(map.get(key))); 
                json.append(","); 
            } 
            json.setCharAt(json.length() - 1, '}'); 
        } else { 
            json.append("}"); 
        } 
        return json.toString(); 
    } 
   
   /**
    * 一个Set集合转换为json格式 
    *@descript
    *@param set
    *@return
    *@author lijianning
    *@date 2015年6月15日
    *@version 1.0v
    */
    public static String setToJson(Set<?> set) { 
        StringBuilder json = new StringBuilder(); 
        json.append("["); 
        if (set != null && set.size() > 0) { 
            for (Object obj : set) { 
                json.append(objectToJson(obj)); 
                json.append(","); 
            } 
            json.setCharAt(json.length() - 1, ']'); 
        } else { 
            json.append("]"); 
        } 
        return json.toString(); 
    } 
    /**
     * json字符串转换为List
     *@descript
     *@param jsonStr
     *@return
     *@author lijianning
     *@date 2015年6月15日
     *@version 1.0v
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> parseJSONList(String jsonStr){  
        JSONArray jsonArr = JSONArray.fromObject(jsonStr);  
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
        Iterator<JSONObject> it = jsonArr.iterator();  
        while(it.hasNext()){  
            JSONObject JSON = it.next();  
            list.add(parseJSONMap(JSON.toString()));  
        }  
        return list;  
    }  
      
     /**
      * json字符串转换为map
      *@descript
      *@param jsonStr
      *@return
      *@author lijianning
      *@date 2015年6月15日
      *@version 1.0v
      */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseJSONMap(String jsonStr){  
        Map<String, Object> map = new HashMap<String, Object>();  
        try {
            //最外层解析  
            JSONObject json = JSONObject.fromObject(jsonStr);
            for (Object k : json.keySet()) {
                Object v = json.get(k);
                //如果内层还是数组的话，继续解析  
                if (v instanceof JSONArray) {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    Iterator<JSONObject> it = ((JSONArray) v).iterator();
                    while (it.hasNext()) {
                        JSONObject JSON = it.next();
                        list.add(parseJSONMap(JSON.toString()));
                    }
                    map.put(k.toString(), list);
                } else {
                    map.put(k.toString(), v);
                }
            } 
        } catch (Exception e) {
            map.put("exception", jsonStr);
        }
        return map;  
    }  
      
     /**
      * 根据一个url地址.获取json数据.转换为List
      *@descript
      *@param url
      *@return
      *@author lijianning
      *@date 2015年6月15日
      *@version 1.0v
      */
    public static List<Map<String, Object>> getListByUrl(String url){  
        try {  
            //通过HTTP获取JSON数据  
            InputStream in = new URL(url).openStream();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
            StringBuilder sb = new StringBuilder();  
            String line;  
            while((line=reader.readLine())!=null){  
                sb.append(line);  
            }  
            return parseJSONList(sb.toString());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
      
     /**
      * 根据一个url地址.获取json数据.转换为MAP
      *@descript
      *@param url
      *@return
      *@author lijianning
      *@date 2015年6月15日
      *@version 1.0v
      */
    public static Map<String, Object> getMapByUrl(String url){  
        try {  
            //通过HTTP获取JSON数据  
            InputStream in = new URL(url).openStream();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
            StringBuilder sb = new StringBuilder();  
            String line;  
            while((line=reader.readLine())!=null){  
                sb.append(line);  
            }  
            return parseJSONMap(sb.toString());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  

}
