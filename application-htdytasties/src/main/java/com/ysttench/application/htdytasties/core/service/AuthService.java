package com.ysttench.application.htdytasties.core.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * 接口
 * 
 * @author Howard
 *
 */
@WebService(targetNamespace = "http://service.core.htdytasties.application.ysttench.com/")
public interface AuthService {
    @WebMethod
    public String getEquipMsg();
}
