package com.ysttench.application.auth.core.service;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.ysttench.application.auth.core.rdto.ApplicationInfo;
import com.ysttench.application.auth.core.rdto.DeptmentInfo;
import com.ysttench.application.auth.core.rdto.RoleInfo;
import com.ysttench.application.auth.core.rdto.SystemsettingInfo;
import com.ysttench.application.auth.core.rdto.UserInfo;

/**
 * 用户接口
 * 
 * @author Howard
 *
 */
@WebService
public interface AuthService {

    /**
     * 应用用户注册
     * @param accessToken
     * @param userInfo
     * @return
     */
    @WebMethod
    public String register(String accessToken, UserInfo userInfo);

    /**
     * 应用子系统用户登陆
     * @param accessToken
     * @param appKey
     * @param userName
     * @param password
     * @return
     */
    @WebMethod
    public String doUserLogin(String accessToken, String appKey, String userName, String password);

    /**
     * 应用子系统用户登陆
     * @param accessToken
     * @param appKey
     * @param name
     * @param sfzh
     * @return
     */
    @WebMethod
    public String doUserLogin4Pki(String accessToken, String appKey, String name, String sfzh);

    /**
     * 获取AccessToken
     * 
     * @return
     */
    @WebMethod
    public String getAccessToken();

    /**
     * 应用系统接入信息保存
     * @param accessToken
     * @param applicationInfo
     * @return
     */
    @WebMethod
    public String saveApplicationInfo(String accessToken, ApplicationInfo applicationInfo);

    /**
     * 应用系统接入信息更新
     * @param accessToken
     * @param applicationInfo
     * @return
     */
    @WebMethod
    public String updateApplicationInfo(String accessToken, String sysKey, ApplicationInfo applicationInfo);

    /**
     * 应用系统接入appKey存在check
     * @param accessToken
     * @param appKey
     * @return
     */
    @WebMethod
    public String checkSysKey(String accessToken, String appKey);

    /**
     * 创建用户
     * @param accessToken
     * @param userInfo
     * @return
     */
    @WebMethod
    public String createUser(String accessToken, UserInfo userInfo);

    /**
     * 批量创建用户
     * 
     * @param accessToken
     * @param userInfolist
     * @return
     */
    @WebMethod
    public String createUserList(String accessToken, List<UserInfo> userInfolist);

    /**
     * 更新用户
     * 
     * @param UserInfo
     * @return
     */
    @WebMethod
    public String updateUser(String accessToken, String userName, UserInfo userInfo);

    /**
     * 删除用户
     * 
     * @param accessToken
     * @param userName
     * @return
     * @throws Exception
     */
    @WebMethod
    public String deleteUser(String accessToken, String userName);

    /**
     * 批量删除用户
     * 
     * @param accessToken
     * @param userNameList
     * @return
     */
    @WebMethod
    public String deleteUserList(String accessToken, List<String> userNameList);

    /**
     * 获取用户信息
     * 
     * @param accessToken
     * @param username
     * @return
     */
    @WebMethod
    public String getUserInfo(String accessToken, String userName);

    /**
     * 获取用户角色信息
     * 
     * @param accessToken
     * @param userName
     * @return
     */
    @WebMethod
    public String getUserRole(String accessToken, String userName);

    /**
     * 获取角色用户信息
     * 
     * @param accessToken
     * @param roleName
     * @return
     */
    @WebMethod
    public String getRoleUser(String accessToken, String roleName);

    /**
     * 用户分配角色
     * 
     * @param accessToken
     * @param userName
     * @param roleName
     * @return
     */
    @WebMethod
    public String chooseUserRole(String accessToken, String userName, String roleName);

    /**
     * 用户批量分配角色
     * 
     * @param accessToken
     * @param userName
     * @param roleNameList
     * @return
     */
    @WebMethod
    public String chooseUserRoleList(String accessToken, String userName, List<String> roleNameList);

    /**
     * 用户分配角色
     * 
     * @param accessToken
     * @param userName
     * @param roleName
     * @return
     */
    @WebMethod
    public String deleteUserRole(String accessToken, String userName, String roleName);

    /**
     * 用户批量分配角色
     * 
     * @param accessToken
     * @param userName
     * @param roleNameList
     * @return
     */
    @WebMethod
    public String deleteUserRoleList(String accessToken, String userName, List<String> roleNameList);

    /**
     * 创建角色
     * 
     * @param accessToken
     * @param roleInfo
     * @return
     */
    @WebMethod
    public String createRole(String accessToken, RoleInfo roleInfo);

    /**
     * 更新角色
     * 
     * @param accessToken
     * @param roleInfo
     * @return
     */
    @WebMethod
    public String updateRole(String accessToken, String roleKey, RoleInfo roleInfo);

    /**
     * 删除角色
     * 
     * @accessToken
     * @param roleInfo
     * @return
     */
    @WebMethod
    public String deleteRole(String accessToken, String roleKey);

    /**
     * 获取角色信息
     * 
     * @param accessToken
     * @param roleInfo
     * @return
     */
    @WebMethod
    public String getRoleInfo(String accessToken, String roleKey);

    /**
     * 创建系统参数
     * 
     * @param accessToken
     * @param info
     * @return
     */
    @WebMethod
    public String createSystemSetting(String accessToken, SystemsettingInfo info);

    /**
     * 更新系统参数
     * 
     * @param accessToken
     * @param info
     * @return
     */
    @WebMethod
    public String updateSystemSetting(String accessToken, String sysKey, SystemsettingInfo info);

    /**
     * 删除系统参数
     * 
     * @param accessToken
     * @param info
     * @return
     */
    @WebMethod
    public String deleteSystemSetting(String accessToken, String sysKey);

    /**
     * 查询系统参数
     * 
     * @param accessToken
     * @param key
     * @return
     */
    @WebMethod
    public String getSystemSetting(String accessToken, String key);

    /**
     * 获取所有角色信息
     * 
     * @param accessToken
     * @return
     */
    @WebMethod
    public String getRoleAll(String accessToken);

    /**
     * 创建部门
     * 
     * @param info
     * @return
     */
    @WebMethod
    public String createDept(String accessToken, DeptmentInfo info);

    /**
     * 删除部门
     * 
     * @param deptCode
     * @return
     */
    @WebMethod
    public String deleteDept(String accessToken, String deptCode);

    /**
     * 批量删除部门
     * 
     * @param deptCode
     * @return
     */
    @WebMethod
    public String deleteDeptlist(String accessToken, List<String> deptCodelist);

    /**
     * 获取部门信息
     * 
     * @param deptCode
     * @return
     */
    @WebMethod
    public String getDeptInfo(String accessToken, String deptCode);

    /**
     * 获取所有部门信息
     * 
     * @return
     */
    @WebMethod
    public String getAllDeptInfo(String accessToken);

    /**
     * 更新部门
     * 
     * @param deptCode
     * @param info
     * @return
     */
    @WebMethod
    public String updateDept(String accessToken, String deptCode, DeptmentInfo info);

}
