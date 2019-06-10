import java.io.IOException;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.ysttench.application.common.server.CXFClientUtil;
import com.ysttench.application.htbw.core.service.AuthService;

public class WsdlTest {/*
    // 将字节数组转换为16进制字符串
    public static String BinaryToHexString(byte[] bytes) {
	String hexStr = "0123456789ABCDEF";
	String result = "";
	String hex = "";
	for (byte b : bytes) {
	    hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
	    hex += String.valueOf(hexStr.charAt(b & 0x0F));
	    result += hex;
	}
	return result;
    }

    // 16进制字符串转换成10进制(温湿度)
    public static double HexStringToDecimal(String HexString) {
	return Long.parseLong(HexString, 16) / 10.0;

    }
*/
public static void main(String[] args) {
    String resultJson = CXFClientUtil
		.getInterfaceService("http://localhost:8083/htbw/services/AuthService?wsdl", AuthService.class).getWarnMsg("", "1");
System.out.println(resultJson);

}   
   
}