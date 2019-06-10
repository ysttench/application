package com.ysttench.application.common.server;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * CXF远程调用webservice
 * 
 * @author wujun
 * 
 */
public class CXFClientUtil {
    
    /**
     * 获取cxf webservice接口实体类
     * @param url
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInterfaceService(String url, Class<T> clazz) {
        JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();
        svr.setServiceClass(clazz);
        svr.setAddress(url);
        T hw = (T) svr.create();
        return hw;
    }
    
    /**
     * 获取cxf webservice接口实体类
     * @param url
     * @param clazz
     * @return
     */
    public static Object[] getServiceResult(String url, String method, Object... param) throws Exception {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(url);
        //getUser 为接口中定义的方法名称  张三为传递的参数   返回一个Object数组
        return client.invoke(method, param);
    }
}
