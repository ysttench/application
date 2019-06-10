package com.ysttench.application.htbw.core.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 接口
 * 
 * @author Howard
 *
 */
@WebService(targetNamespace = "http://service.core.htbw.application.ysttench.com/")
public interface AuthService {
    /**
     * 登陆验证接口
     * 
     * @param userName
     * @param password
     * @return
     */
    @WebMethod
    public String login(@WebParam(name = "userName")String userName,@WebParam(name = "password")String password);

    /**
     * 密码修改接口
     * 
     * @param userName
     * @param oldpassword
     * @param newpassword
     * @return
     */
    @WebMethod
    public String updatepassword(@WebParam(name = "userName") String userName,
	    @WebParam(name = "oldpassword") String oldpassword, @WebParam(name = "newpassword") String newpassword);

    /**
     * 获取设备信息
     * 
     * @param checkValue
     * @return
     */
    @WebMethod
    public String getEquipmentMsg(@WebParam(name = "checkValue") String checkValue);

    /**
     * 获取警告信息
     * 
     * @param checkValue
     * @return
     */
    @WebMethod
    public String getWarnMsg(@WebParam(name = "checkValue") String checkValue,@WebParam(name = "time") String time);

    /**
     * 获取交班记录
     * 
     * @param userName
     * @param checkValue
     * @return
     */
    @WebMethod
    public String getlogRecord(@WebParam(name = "userName") String userName,
	    @WebParam(name = "checkValue") String checkValue);

    /**
     * 通知公告
     * 
     * @param checkValue
     * @return
     */
    @WebMethod
    public String getAnnouncement(@WebParam(name = "checkValue") String checkValue);
    /**
     * 添加上班记录
     * 
     * @param userName
     * @return
     */
    @WebMethod
    public String addslogRecord(@WebParam(name = "userName") String userName);
    /**
     * 添加交班记录
     * 
     * @param userName
     * @param jbDesc
     * @return
     */
    @WebMethod
    public String addlogRecord(@WebParam(name = "recordId") String recordId,@WebParam(name = "jbDesc") String jbDesc);
    /**
     * 实时监控
     * @return
     */
    @WebMethod
    public String  monitor();
    /**
     * 获取设备故障信息
     * @return
     */
    @WebMethod
    public String getwitchRecord();
    /**
     * 获取今天的设备数据
     * @return
     */
    @WebMethod
    public String getbEquipmentMsg(@WebParam(name = "equipmentId")String equipmentId);
}
