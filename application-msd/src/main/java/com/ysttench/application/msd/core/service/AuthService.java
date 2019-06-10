package com.ysttench.application.msd.core.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * 接口
 * 
 * @author Howard
 *
 */
@WebService(targetNamespace = "http://service.core.msd.application.ysttench.com/")
public interface AuthService {
	@WebMethod
	public String getBrightElement();
	@WebMethod
	public String checkBrightElement(String id);
	/**
	 * 已存物品列表
	 * @return
	 */
	@WebMethod
	public String getElementList(String type);
	/**
	 * 取物
	 * @return
	 */
	@WebMethod
	public String getElement(String msdNum);
	/**
	 * 强行取出
	 * @return
	 */
	@WebMethod
	public String getqElement(String msdNum,String qpassword);
	/**
	 * 存物
	 * @return
	 */
	public String saveElement(String msdNum,String arkCode);
	/**
	 * 关灯
	 * @param ip
	 * @param port
	 * @param point
	 * @return
	 */
	public String colseElement(String ip,String port,String point,String flag,String msdNum);
}
