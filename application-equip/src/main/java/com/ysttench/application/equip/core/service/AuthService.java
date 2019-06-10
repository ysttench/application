package com.ysttench.application.equip.core.service;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import com.ysttench.application.equip.core.rdto.EquipException;

/**
 * 接口
 * 
 * @author Howard
 *
 */
@WebService(targetNamespace = "http://service.core.equip.application.ysttench.com/")
public interface AuthService {
	@WebMethod
	public String exceptionMsg(List<EquipException> list);
	@WebMethod
	public String exceptionMsg2(String jobj);
}
