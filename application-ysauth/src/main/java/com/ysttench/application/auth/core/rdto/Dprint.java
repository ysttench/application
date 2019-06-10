package com.ysttench.application.auth.core.rdto;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.ysttench.application.auth.settings.kernel.entity.SysPrintForMap;

public class Dprint {	
	public  String getRemoteInfo(SysPrintForMap print,String organ,String T,String QR_T) throws Exception{
	    String WSDL_URI = print.get("wsdlUrl").toString();//wsdl 的uri
	    String namespace = print.get("nameSpace").toString();//namespace
	    String methodName =  print.get("module").toString();//要调用的方法名称

	    SoapObject request = new SoapObject(namespace, methodName);
	    // 设置需调用WebService接口需要传入的参数
	    request.addProperty("x", print.get("xNum").toString());
	    request.addProperty("y", print.get("yNum").toString());
	    request.addProperty("Text", T);
	    request.addProperty("QR_T", QR_T);

	    //创建SoapSerializationEnvelope 对象，同时指定soap版本号
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER12);
	    envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
	    envelope.dotNet=true;

	    HttpTransportSE httpTransportSE = new HttpTransportSE(WSDL_URI);
	    httpTransportSE.call(null, envelope);//调用

	    // 获取返回的数据
	    SoapObject object = (SoapObject) envelope.bodyIn;
	    // 获取返回的结果
	    String result = object.getProperty(0).toString();
	    
	    return result;

	}
}
