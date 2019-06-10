import java.util.ArrayList;
import java.util.List;

import com.ysttench.application.common.server.CXFClientUtil;
import com.ysttench.application.equip.core.rdto.EquipException;
import com.ysttench.application.equip.core.service.AuthService;

public class test {
public static void main(String[] args) {
List<EquipException> list = new ArrayList<EquipException>();
EquipException s= new EquipException();
s.setDescription("1111");
s.setDracution("111");
//s.setEndTime(endTime);
s.setEquipmentNum("TN001");
s.setExceptCode("SW000");
//s.setStartTime(startTime);
list.add(s);
	String resultJson = CXFClientUtil
			.getInterfaceService("http://192.168.1.107:8080/equip/services/AuthService?wsdl", AuthService.class).exceptionMsg(list);
	System.out.println(resultJson);

}
}
