import com.ysttench.application.auth.core.service.AuthService;
import com.ysttench.application.common.server.CXFClientUtil;

public class WsdlTest {
public static void main(String[] args) throws Exception {
	String resultJson = CXFClientUtil
			.getInterfaceService("http://localhost:55013/auth/services/AuthService?wsdl", AuthService.class).checkDetailPd("10029", "BJ001", "admin");
	//String resultJson = CXFClientUtil
	//		.getInterfaceService("http://10.0.216.98:8010/auth/services/AuthService?wsdl", AuthService.class).login("郭红星", "ghx863863");
	System.out.println(resultJson);
}
}