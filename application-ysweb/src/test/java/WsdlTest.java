

import com.ysttench.application.common.server.CXFClientUtil;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.web.core.service.AuthService;

public class WsdlTest {
public static void main(String[] args) {
	System.out.println(DatetimeUtil.getDateyyyyMMdd());
	String resultJson = CXFClientUtil
			.getInterfaceService("http://localhost:8008/ysweb/services/AuthService?wsdl", AuthService.class).checkGdPd("100002", "1", "100006");
	//String resultJson = CXFClientUtil
	//		.getInterfaceService("http://10.0.216.98:8010/auth/services/AuthService?wsdl", AuthService.class).getwarehouse("oa", "159654");
	//System.out.println(resultJson);
	System.out.println(resultJson);
}
}
