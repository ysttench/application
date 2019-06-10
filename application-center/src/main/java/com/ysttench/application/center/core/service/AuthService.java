package com.ysttench.application.center.core.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * 接口
 * 
 * @author Howard
 *
 */
@WebService(targetNamespace = "http://service.core.center.application.ysttench.com/")
public interface AuthService {

    @WebMethod
    String register(String companyName, String email, String mobile, String macAddress);

}
