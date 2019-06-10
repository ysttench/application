package com.ysttench.application.auth.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ysttench.application.auth.core.rdto.ApplicationInfo;
import com.ysttench.application.auth.core.rdto.DeptmentInfo;
import com.ysttench.application.auth.core.rdto.ResponseDto;
import com.ysttench.application.auth.core.rdto.RoleInfo;
import com.ysttench.application.auth.core.rdto.SystemsettingInfo;
import com.ysttench.application.auth.core.rdto.UserInfo;
import com.ysttench.application.auth.core.service.AuthService;
import com.ysttench.application.auth.settings.kernel.entity.ApiAccessTokenFormMap;
import com.ysttench.application.auth.settings.kernel.entity.ApiLogUserLoginFormMap;
import com.ysttench.application.auth.settings.kernel.entity.ApiRoleFormMap;
import com.ysttench.application.auth.settings.kernel.entity.ApiSystemInfoFormMap;
import com.ysttench.application.auth.settings.kernel.entity.ApiSystemSettingFormMap;
import com.ysttench.application.auth.settings.kernel.entity.ApiUserFormMap;
import com.ysttench.application.auth.settings.kernel.entity.ApiUserRoleFormMap;
import com.ysttench.application.auth.settings.kernel.entity.SysDepartmentFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.ApiAccessTokenMapper;
import com.ysttench.application.auth.settings.kernel.mapper.ApiLogUserLoginMapper;
import com.ysttench.application.auth.settings.kernel.mapper.ApiRoleMapper;
import com.ysttench.application.auth.settings.kernel.mapper.ApiSystemInfoMapper;
import com.ysttench.application.auth.settings.kernel.mapper.ApiSystemSettingMapper;
import com.ysttench.application.auth.settings.kernel.mapper.ApiUserMapper;
import com.ysttench.application.auth.settings.kernel.mapper.ApiUserRoleMapper;
import com.ysttench.application.auth.settings.kernel.mapper.SysDepartmentMapper;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.BeanUtil;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.common.util.encrypt.Encrypt;


/**
 * 用户接口实现
 * 
 * @author 潘益孟
 * 
 */
@WebService(targetNamespace = "http://service.core.auth.application.ysttench.com/", endpointInterface = "com.ysttench.application.auth.core.service.AuthService")
public class AuthServiceImpl implements AuthService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private ApiUserMapper apiUserMapper;

    @Inject
    private ApiUserRoleMapper apiUserRoleMapper;

    @Inject
    private ApiRoleMapper apiRoleMapper;
    @Inject
    private SysDepartmentMapper sysDepartmentMapper;

    @Autowired
    private ApiSystemSettingMapper apiSystemSettingMapper;

    @Autowired
    private ApiAccessTokenMapper apiAccessTokenMapper;

    @Autowired
    private ApiSystemInfoMapper apiSystemInfoMapper;
    @Autowired
    private ApiLogUserLoginMapper apiLogUserLoginMapper;

    // 密码加密
    private Encrypt encrypt = new Encrypt();

    /* 
     * 应用子系统注册用户
     */
    @Override
    public String register(String accessToken, UserInfo userInfo) {
        ResponseDto responseDto = new ResponseDto();
        ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            apiUserFormMap.putAll(BeanUtil.bean2Map(userInfo));
            apiUserFormMap.put("password", encrypt.encoder(userInfo.getPassword()));// 密码加密
            apiUserFormMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            apiUserMapper.insertApiUser(apiUserFormMap);
            responseDto.setErrcode("0");// 校验码 ,1是失败,0是成功
            responseDto.setErrmsg("注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("注册异常");
            responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
            responseDto.setErrmsg("注册异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 用户登录
     */
    @Override
    public String doUserLogin(String accessToken, String appKey, String userName, String password) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
            apiUserFormMap.put("where", "where ENABLED_FLAG='0' AND DELETE_FLAG='0' AND USER_NAME='" + userName + "'");
            List<ApiUserFormMap> apiUserForMapList = apiUserMapper.findByWhere(apiUserFormMap);
            if (apiUserForMapList == null || apiUserForMapList.isEmpty()) {
                responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
                responseDto.setErrmsg("获取用户信息失败！");
                return JsonUtils.bean2json(responseDto);
            }
            if (apiUserForMapList.get(0).containsKey("password") 
                    && encrypt.decoder(apiUserForMapList.get(0).getStr("password")).equals(password)) {
                this.saveLog(appKey, userName);
                responseDto.setErrcode("0");// 校验码 ,1是失败,0是成功
                responseDto.setErrmsg("登陆成功！");
            } else {
                responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
                responseDto.setErrmsg("登陆失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户登陆异常");
            responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
            responseDto.setErrmsg("用户登陆异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 用户登录
     */
    @Override
    public String doUserLogin4Pki(String accessToken, String appKey, String name, String sfzh) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
            apiUserFormMap.put("where", "where ENABLED_FLAG='0' AND DELETE_FLAG='0' AND NAME='" + name + "'");
            List<ApiUserFormMap> apiUserForMapList = apiUserMapper.findByWhere(apiUserFormMap);
            if (apiUserForMapList == null || apiUserForMapList.isEmpty()) {
                responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
                responseDto.setErrmsg("获取用户信息失败！");
                return JsonUtils.bean2json(responseDto);
            }
            if (apiUserForMapList.get(0).containsKey("sfzh") 
                    && apiUserForMapList.get(0).getStr("sfzh").equals(sfzh)) {
                this.saveLog(appKey, apiUserForMapList.get(0).getStr("userName"));
                responseDto.setErrcode("0");// 校验码 ,1是失败,0是成功
                responseDto.setErrmsg("登陆成功！");
            } else {
                responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
                responseDto.setErrmsg("登陆失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户登陆异常");
            responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
            responseDto.setErrmsg("用户登陆异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 保存登陆履历
     * @param apiLogUserLogin
     * @return
     */
    private Boolean saveLog(String appKey, String userName) {
        try {
            
            ApiUserFormMap param = new ApiUserFormMap();
            param.put("userName", userName);
            List<ApiUserFormMap> listApiUserFormMap = this.apiUserMapper.findByNames(param);
            if (listApiUserFormMap == null || listApiUserFormMap.isEmpty()) return Boolean.FALSE;
        
            ApiLogUserLoginFormMap apiLogUserLoginFormMap = new ApiLogUserLoginFormMap();
            apiLogUserLoginFormMap.put("userId", listApiUserFormMap.get(0).getInt("userId"));
            apiLogUserLoginFormMap.put("userName", userName);
            apiLogUserLoginFormMap.put("applicationKey", appKey);
            apiLogUserLoginFormMap.put("loginIp", SessionUtil.getIpAddress());
            apiLogUserLoginFormMap.put("loginTime", DatetimeUtil.getDate());
            apiLogUserLoginMapper.addEntity(apiLogUserLoginFormMap);
            return Boolean.TRUE;
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    /**
     * 获取AccessToken
     * 
     * @param userName
     * @param password
     * @return
     */
    @Override
    public String getAccessToken() {
        
//        ResponseDto responseDto = new ResponseDto();
//        UserInfo userInfo = new UserInfo();
//        userInfo.setUserName(userName);
//        userInfo.setPassword(password);
        
        try {
            // 获取会话session
            Message message = PhaseInterceptorChain.getCurrentMessage();
            HttpServletRequest request = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
            HttpSession session = request.getSession(true);
//            if (!checkNamePassword(userInfo, responseDto)) {
//                return JsonUtils.bean2json(responseDto);
//            }
//
//            // 登录验证
//            if (!checkUser(userName, password, responseDto)) {
//                return JsonUtils.bean2json(responseDto);
//            }

//            String userId = responseDto.getResponseObject().toString();
            // 保存会话信息
            ApiAccessTokenFormMap apiAccessTokenFormMap = saveAccessToken(session);
//            List<ApiAccessTokenFormMap> lists = apiAccessTokenMapper.findByAttribute("USER_ID", userId,
//                    ApiAccessTokenFormMap.class);
//            if (lists == null || lists.size() == 0) {
//                accessTokenMsg(query, userId);
//                apiAccessTokenMapper.addEntity(query);
//            } else {
//                accessTokenMsg(query, userId);
//                query.set("id", lists.get(0).get("id"));
//                apiAccessTokenMapper.editEntity(query);
//            }
//            responseDto.setErrcode("0");// 校验成功
//            responseDto.setErrmsg("创建accessToken成功！");
//            responseDto.setResponseObject(apiAccessTokenFormMap.get("accessToken"));
            return apiAccessTokenFormMap.getStr("accessToken");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建accessToken异常");
//            responseDto.setErrcode("1");
//            responseDto.setErrmsg("创建accessToken异常");
            return "";
        }
    }

    /*
     * 应用系统接入信息更新
     */
    @Override
    public String saveApplicationInfo(String accessToken, ApplicationInfo applicationInfo) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            ApiSystemInfoFormMap paramFormMap = new ApiSystemInfoFormMap();
            paramFormMap.put("sysKey", applicationInfo.getSysKey());
            List<ApiSystemInfoFormMap> listApiSystemInfoFormMap = this.apiSystemInfoMapper.findByNames(paramFormMap);
            // 新增应用系统配置信息
            if (listApiSystemInfoFormMap == null || listApiSystemInfoFormMap.isEmpty()) {
                ApiSystemInfoFormMap apiSystemInfoFormMap = new ApiSystemInfoFormMap();
                apiSystemInfoFormMap.putAll(BeanUtil.bean2Map(applicationInfo));
                apiSystemInfoFormMap.put("deletedFlag", "0");
                apiSystemInfoFormMap.put("createTime", DatetimeUtil.getDate());
                this.apiSystemInfoMapper.addEntity(apiSystemInfoFormMap);
                responseDto.setErrcode("0");// 校验码 ,1是失败,0是成功
                responseDto.setErrmsg("应用系统接入信息更新成功！");
            } else {
                responseDto.setResponseObject("1");
                responseDto.setErrmsg("应用系统接入信息保存失败---sysKey已经被使用！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("应用系统接入信息保存异常");
            responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
            responseDto.setErrmsg("应用系统接入信息保存异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /*
     * 应用系统接入信息更新
     */
    @Override
    public String updateApplicationInfo(String accessToken, String sysKey, ApplicationInfo applicationInfo) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            ApiSystemInfoFormMap paramFormMap = new ApiSystemInfoFormMap();
            paramFormMap.put("sysKey", applicationInfo.getSysKey());
            List<ApiSystemInfoFormMap> listApiSystemInfoFormMap = this.apiSystemInfoMapper.findByNames(paramFormMap);
            // 新增应用系统配置信息
            if (listApiSystemInfoFormMap == null || listApiSystemInfoFormMap.isEmpty()) {
                responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
                responseDto.setErrmsg("应用系统接入信息更新失败-----指定sysKey[" + sysKey + "]不存在！");
            } else {
                ApiSystemInfoFormMap apiSystemInfoFormMap = new ApiSystemInfoFormMap();
                apiSystemInfoFormMap.putAll(BeanUtil.bean2Map(applicationInfo));
                apiSystemInfoFormMap.put("id", listApiSystemInfoFormMap.get(0).getInt("id"));
                this.apiSystemInfoMapper.editEntity(apiSystemInfoFormMap);
                responseDto.setErrcode("0");// 校验码 ,1是失败,0是成功
                responseDto.setErrmsg("应用系统接入信息更新成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("应用系统接入信息更新异常");
            responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
            responseDto.setErrmsg("应用系统接入信息更新异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /*
     * 应用系统接入appKey存在check
     */
    @Override
    public String checkSysKey(String accessToken, String appKey) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            ApiSystemInfoFormMap paramFormMap = new ApiSystemInfoFormMap();
            paramFormMap.put("sysKey", appKey);
            List<ApiSystemInfoFormMap> listApiSystemInfoFormMap = this.apiSystemInfoMapper.findByNames(paramFormMap);
            // 新增应用系统配置信息
            if (listApiSystemInfoFormMap == null || listApiSystemInfoFormMap.isEmpty()) {
                responseDto.setResponseObject(true);
            } else {
                responseDto.setResponseObject(false);
            }
            responseDto.setErrcode("0");// 校验码 ,1是失败,0是成功
            responseDto.setErrmsg("应用系统接入appKey存在检查成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("应用系统接入appKey存在检查异常");
            responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
            responseDto.setErrmsg("应用系统接入appKey存在检查异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 创建用户
     * 
     * @param UserInfo
     * @return
     */
    @Override
    public String createUser(String accessToken, UserInfo userInfo) {
        ResponseDto responseDto = new ResponseDto();
        ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkUserParameter(userInfo, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 判断是否已被注册
            if (checkUserColumn(userInfo.getUserName(), "用户名", "USER_NAME", responseDto)
                    && checkUserColumn(userInfo.getSfzh(), "身份证号", "SFZH", responseDto)
                    && checkUserColumn(userInfo.getPhone(), "手机号", "PHONE", responseDto)
                    && checkUserColumn(userInfo.getEmail(), "邮箱", "EMAIL", responseDto)) {

                apiUserFormMap.putAll(BeanUtil.bean2Map(userInfo));
                addOther(apiUserFormMap);// 添加javabean没有的其他字段
                //apiUserFormMap.put("password", encrypt.encoder(userInfo.getPassword()));// 密码加密
                apiUserMapper.insertApiUser(apiUserFormMap);
                responseDto.setErrcode("0");// 校验码 ,1是失败,0是成功
                responseDto.setErrmsg("添加成功");
                responseDto.setResponseObject(apiUserFormMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加账号异常");
            responseDto.setErrcode("1");// 校验码 ,1是失败,0是成功
            responseDto.setErrmsg("添加账号异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 批量创建用户
     * 
     * @param accessToken
     * @param userInfoList
     * @return
     */
    @Override
    public String createUserList(String accessToken, List<UserInfo> userInfoList) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkUserList(userInfoList, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            List<ApiUserFormMap> list = new ArrayList<ApiUserFormMap>();
            for (int i = 0; i < userInfoList.size(); i++) {
                ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
                UserInfo userInfo = userInfoList.get(i);
                if (checkUserColumn(userInfo.getUserName(), "用户名", "USER_NAME", i + 1, responseDto)
                        && checkUserColumn(userInfo.getSfzh(), "身份证号", "SFZH", i + 1, responseDto)
                        && checkUserColumn(userInfo.getPhone(), "手机号", "MOBILE", i + 1, responseDto)
                        && checkUserColumn(userInfo.getEmail(), "邮箱", "EMAIL", i + 1, responseDto)) {
                    apiUserFormMap.putAll(BeanUtil.bean2Map(userInfo));
                    apiUserFormMap.put("password", encrypt.encoder(userInfo.getPassword()));
                    addOther(apiUserFormMap);
                    list.add(apiUserFormMap);
                } else {
                    return JsonUtils.bean2json(responseDto);
                }
            }
            // 批量创建用户
            apiUserMapper.batchSave(list);
            responseDto.setErrcode("0");
            responseDto.setErrmsg("批量创建成功");
            responseDto.setResponseObject(list);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("批量创建账号异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("批量创建账号异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 更新用户
     * 
     * @param UserInfo
     * @return
     */
    @Override
    public String updateUser(String accessToken, String userName, UserInfo userInfo) {
        ResponseDto responseDto = new ResponseDto();
        ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkUserParameterUpdate(userInfo, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 指定用户数据存在check
            if (!checkUserIdByDB(userName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 判断是否已被注册
            if (checkUserColumn(userInfo.getUserName(), "用户名", "USER_NAME", userName, responseDto)
                    && checkUserColumn(userInfo.getSfzh(), "身份证号", "SFZH", userName, responseDto)
                    && checkUserColumn(userInfo.getPhone(), "手机号", "PHONE", userName, responseDto)
                    && checkUserColumn(userInfo.getEmail(), "邮箱", "EMAIL", userName, responseDto)) {

                apiUserFormMap.putAll(BeanUtil.bean2Map(userInfo));
                apiUserFormMap.put("userId", responseDto.getResponseObject());
                apiUserFormMap.put("updateTime", DatetimeUtil.getDate());
                apiUserMapper.editApiUSer(apiUserFormMap);
                responseDto.setErrcode("0");
                responseDto.setErrmsg("更新成功");
                responseDto.setResponseObject(apiUserFormMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新账号异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("更新账号异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 删除用户
     * 
     * @param accessToken
     * @param userName
     * @return
     * @throws Exception
     */
    @Override
    public String deleteUser(String accessToken, String userName) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkUserName(userName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 指定用户数据存在check
            if (!checkUserNameByDB(userName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
            apiUserFormMap.put("userId", responseDto.getResponseObject());
            apiUserFormMap.put("userName", userName);
            apiUserFormMap.put("deleteFlag", "1");
            apiUserMapper.editEntity(apiUserFormMap);
            responseDto.setErrcode("0");
            responseDto.setErrmsg("删除成功");
            responseDto.setResponseObject(apiUserFormMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除账号异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("删除账号异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 批量删除用户
     * 
     * @param accessToken
     * @param userNameList
     * @return
     */
    @Override
    public String deleteUserList(String accessToken, List<String> userNameList) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkUserNameList(userNameList, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 指定用户数据存在check
            List<String> listParam = new ArrayList<String>();
            if (checkUserNameListByDB(userNameList, listParam, responseDto)) {

                for (String userId : listParam) {
                    ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
                    apiUserFormMap.put("userId", userId);
                    apiUserFormMap.put("deleteFlag", 1);
                    apiUserFormMap.put("deleteTime", DatetimeUtil.fromDateH());
                    apiUserMapper.editEntity(apiUserFormMap);
                }
                responseDto.setErrcode("0");
                responseDto.setErrmsg("批量删除成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("批量删除账号异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("批量删除账号异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 获取用户信息
     * 
     * @param accessToken
     * @param username
     * @return
     */
    @Override
    public String getUserInfo(String accessToken, String userName) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkUserName(userName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            ApiUserFormMap apiUserFormMap = apiUserMapper.findbyFrist("USER_NAME", userName, ApiUserFormMap.class);// 用户信息查询
            if (apiUserFormMap == null || apiUserFormMap.isEmpty()) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg(userName + "用户不存在");
                return JsonUtils.bean2json(responseDto);
            }
            String deptId = apiUserFormMap.getStr("deptId");
            SysDepartmentFormMap sysDepartmentFormMap = sysDepartmentMapper.findbyFrist("id", deptId, SysDepartmentFormMap.class);
            UserInfo userInfo = BeanUtil.map2Bean(apiUserFormMap, UserInfo.class);
            userInfo.setDeptName(sysDepartmentFormMap.getStr("deptName"));
            responseDto.setErrcode("0");// 校验成功
            responseDto.setErrmsg("查询成功");
            responseDto.setResponseObject(userInfo);// 返回用户信息
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户信息异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("查询用户信息异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 获取用户角色信息
     * 
     * @param accessToken
     * @param userName
     * @return
     */
    @Override
    public String getUserRole(String accessToken, String userName) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkUserName(userName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            String[] roleName = getRoleIdByUserName(userName, responseDto);
            if (roleName == null) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg("该用户还未拥有角色");
            } else {
                responseDto.setErrcode("0");
                responseDto.setErrmsg("查询成功");
                responseDto.setResponseObject(roleName);// 返回用户信息
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户角色信息异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("查询用户角色信息异常");
        }

        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 获取角色用户信息
     * 
     * @param accessToken
     * @param roleName
     * @return
     */
    @Override
    public String getRoleUser(String accessToken, String roleName) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkRoleName(roleName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            String[] userName = getUserIdByRoleName(roleName, responseDto);
            if (userName == null) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg("该角色未被分配");
            } else {
                responseDto.setErrcode("0");
                responseDto.setErrmsg("查询成功");
                responseDto.setResponseObject(userName);// 返回用户信息
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询角色对应用户信息异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("查询角色对应用户信息异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 用户分配角色
     * 
     * @param accessToken
     * @param userName
     * @param roleName
     * @return
     */
    @Override
    public String chooseUserRole(String accessToken, String userName, String roleName) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkUserNameRoleName(userName, roleName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 获取用户信息
            ApiUserFormMap userInfo = this.getUserInfoByUserName(userName, responseDto);
            if (userInfo == null) {
                return JsonUtils.bean2json(responseDto);
            }
            // 获取角色信息
            ApiRoleFormMap roleInfo = this.getRoleInfoByRoleName(roleName, responseDto);
            if (roleInfo == null) {
                return JsonUtils.bean2json(responseDto);
            }

            // 做用户角色关系存在check
            if (checkUserIdRoleId(userInfo.get("userId").toString(), roleInfo.get("roleId").toString(), responseDto)) {
                ApiUserRoleFormMap apiUserRoleFormMap = new ApiUserRoleFormMap();
                apiUserRoleFormMap.put("userId", userInfo.get("userId"));
                apiUserRoleFormMap.put("roleId", roleInfo.get("roleId"));
                apiUserRoleMapper.addEntity(apiUserRoleFormMap);
                responseDto.setErrcode("0");// 校验成功
                responseDto.setErrmsg("分配角色成功");
                responseDto.setResponseObject(apiUserRoleFormMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户分配角色异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("用户分配角色异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 用户批量分配角色
     * 
     * @param accessToken
     * @param userName
     * @param roleNameList
     * @return
     */
    @Override
    public String chooseUserRoleList(String accessToken, String userName, List<String> roleNameList) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkUserNameRoleNameList(userName, roleNameList, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 获取用户信息
            ApiUserFormMap userInfo = this.getUserInfoByUserName(userName, responseDto);
            if (userInfo == null) {
                return JsonUtils.bean2json(responseDto);
            }

            // 获取用户ID角色ID关系数据。
            List<ApiUserRoleFormMap> list = getUserIdRoleIdList(userName, roleNameList, responseDto);
            if (list == null) {
                return JsonUtils.bean2json(responseDto);
            }
            apiUserRoleMapper.batchSave(list);// //
            responseDto.setErrcode("0");// 校验成功
            responseDto.setErrmsg("分配角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户分配角色异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("用户分配角色异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 删除用户已分配角色
     * 
     * @param accessToken
     * @param userName
     * @param roleName
     * @return
     */
    @Override
    public String deleteUserRole(String accessToken, String userName, String roleName) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkUserNameRoleName(userName, roleName, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 获取用户信息
            ApiUserFormMap userInfo = this.getUserInfoByUserName(userName, responseDto);
            if (userInfo == null) {
                return JsonUtils.bean2json(responseDto);
            }
            // 获取角色信息
            ApiRoleFormMap roleInfo = this.getRoleInfoByRoleName(roleName, responseDto);
            if (roleInfo == null) {
                return JsonUtils.bean2json(responseDto);
            }

            // 做用户角色关系存在check
            if (checkUserIdRoleIdForDel(userInfo.get("userId").toString(), roleInfo.get("roleId").toString(),
                    responseDto)) {
                ApiUserRoleFormMap apiUserRoleFormMap = new ApiUserRoleFormMap();
                apiUserRoleFormMap.put("userId", userInfo.get("userId"));
                apiUserRoleFormMap.put("roleId", roleInfo.get("roleId"));
                apiUserRoleMapper.deleteByNames(apiUserRoleFormMap);
                responseDto.setErrcode("0");// 校验成功
                responseDto.setErrmsg("分配角色成功");
                responseDto.setResponseObject(apiUserRoleFormMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户分配角色异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("用户分配角色异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 批量删除用户已分配角色
     * 
     * @param accessToken
     * @param userName
     * @param roleNameList
     * @return
     */
    @Override
    public String deleteUserRoleList(String accessToken, String userName, List<String> roleNameList) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkUserNameRoleNameList(userName, roleNameList, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 获取用户信息
            ApiUserFormMap userInfo = this.getUserInfoByUserName(userName, responseDto);
            if (userInfo == null) {
                return JsonUtils.bean2json(responseDto);
            }
            for (String roleName : roleNameList) {
                ApiRoleFormMap roleInfo = this.getRoleInfoByRoleName(roleName, responseDto);
                if (roleInfo == null) {
                    return JsonUtils.bean2json(responseDto);
                }
            }
            String userId = userInfo.get("userId").toString();
            // 获取用户ID角色ID关系数据。
            List<ApiUserRoleFormMap> list = getUserIdRoleIdListForDel(userId, roleNameList, responseDto);
            if (list == null) {
                return JsonUtils.bean2json(responseDto);
            }
            for (int i = 0; i < list.size(); i++) {
                apiUserRoleMapper.deleteByNames(list.get(i));
            }
            responseDto.setErrcode("0");// 校验成功
            responseDto.setErrmsg("删除角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户删除角色异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("用户删除角色异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 创建角色
     * 
     * @param roleInfo
     * @return
     */
    @Override
    public String createRole(String accessToken, RoleInfo roleInfo) {
        ResponseDto responseDto = new ResponseDto();
        ApiRoleFormMap apiRoleFormMap = new ApiRoleFormMap();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 创建角色参数判断
            if (!checkRoleParameter(roleInfo, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 判断是否已被注册
            if (checkRoleColumn(roleInfo.getRoleKey(), responseDto)) {
                apiRoleFormMap.putAll(BeanUtil.bean2Map(roleInfo));
                // 添加javabean没有的其他字段
                addOther(apiRoleFormMap, responseDto);
                apiRoleMapper.addEntity(apiRoleFormMap);
                responseDto.setErrcode("0");// 校验成功
                responseDto.setErrmsg("创建角色成功！");
                responseDto.setResponseObject(apiRoleFormMap);
            }
        } catch (Exception e) {
            e.getStackTrace();
            logger.error("角色创建异常");
            responseDto.setErrcode("1");// 校验失败
            responseDto.setErrmsg("角色创建异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 获取角色信息
     * 
     * @param accessToken
     * @param rolename
     * @return
     */
    @Override
    public String getRoleInfo(String accessToken, String roleKey) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
             if (!checkAccessToken(accessToken, responseDto)) {
             return JsonUtils.bean2json(responseDto);
             }

            // 整合性check
            if (!checkRoleName(roleKey, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            List<ApiRoleFormMap> lists = apiRoleMapper.findByAttribute("ROLE_KEY", roleKey, ApiRoleFormMap.class);
            if (lists == null || lists.isEmpty()) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg(roleKey + "角色key不存在");
                return JsonUtils.bean2json(responseDto);
            }
            responseDto.setErrcode("0");// 校验成功
            responseDto.setErrmsg("查询角色信息成功！");
            responseDto.setResponseObject(lists.get(0));// 返回角色信息
        } catch (Exception e) {
            e.getStackTrace();
            logger.error("查询角色信息异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("查询角色信息异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 修改角色
     * 
     * @param roleInfo
     * @return
     */
    @Override
    public String updateRole(String accessToken, String roleKey, RoleInfo roleInfo) {
        ResponseDto responseDto = new ResponseDto();
        ApiRoleFormMap apiRoleFormMap = new ApiRoleFormMap();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkRoleParameterUpdate(roleInfo, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            if (!checkRoleIdByDB(roleKey, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 判断是否已被注册
            if (checkRoleColumn(roleKey, roleInfo, responseDto)) {
                apiRoleFormMap.put("roleId", responseDto.getResponseObject());
                apiRoleFormMap.putAll(BeanUtil.bean2Map(roleInfo));
                apiRoleMapper.editEntity(apiRoleFormMap);
                responseDto.setErrcode("0");
                responseDto.setErrmsg("更新角色信息成功！");
                responseDto.setResponseObject(apiRoleFormMap);
            }
        } catch (Exception e) {
            e.getStackTrace();
            logger.error("更新角色信息异常");
            responseDto.setErrcode("1");// 校验码，1 失败，0 成功
            responseDto.setErrmsg("更新角色信息异常");
            return JsonUtils.bean2json(responseDto);
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 删除角色
     * 
     * @param accessToken
     * @param roleInfo
     * @return
     */
    @Override
    public String deleteRole(String accessToken, String roleKey) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 整合性check
            if (!checkRoleName(roleKey, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 指定角色数据存在check
            if (!checkRoleNameByDB(roleKey, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            ApiRoleFormMap apiRoleFormMap = new ApiRoleFormMap();
            apiRoleFormMap.put("roleId", responseDto.getResponseObject());
            apiRoleFormMap.put("roleKey", roleKey);
            apiRoleFormMap.put("deletedFlag", "1");
            apiRoleMapper.editEntity(apiRoleFormMap);
            responseDto.setErrcode("0");// 校验成功
            responseDto.setErrmsg("删除角色信息成功！");
            responseDto.setResponseObject(apiRoleFormMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除角色信息异常");
            responseDto.setErrcode("1");// 校验码，1 失败，0 成功
            responseDto.setErrmsg("删除角色信息异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 创建系统参数
     * 
     * @param info
     * @return
     */
    @Override
    public String createSystemSetting(String accessToken, SystemsettingInfo info) {
        ResponseDto responseDto = new ResponseDto();
        ApiSystemSettingFormMap apiSystemSettingFormMap = new ApiSystemSettingFormMap();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 创建系统参数参数判断
            if (!checkSystemParameter(info, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 判断是否已被注册
            if (checkSystemColumn(info.getSysKey(), responseDto)) {
                apiSystemSettingFormMap.putAll(BeanUtil.bean2Map(info));
                addOther(apiSystemSettingFormMap, responseDto);// 添加javabean没有的其他字段
                apiSystemSettingMapper.addEntity(apiSystemSettingFormMap);
                responseDto.setErrcode("0");// 校验成功
                responseDto.setErrmsg("创建系统参数成功！");
                responseDto.setResponseObject(apiSystemSettingFormMap);
            }
        } catch (Exception e) {
            e.getStackTrace();
            logger.error("创建系统参数异常");
            responseDto.setErrcode("1");// 校验码，1 失败，0 成功
            responseDto.setErrmsg("创建系统参数异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 获取所有角色信息
     * 
     * @param accessToken
     * @return
     */
    @Override
    public String getRoleAll(String accessToken) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            ApiRoleFormMap apiRoleFormMap = new ApiRoleFormMap();
            String deletedFlag = "0";
            apiRoleFormMap.put("where", "where DELETED_FLAG=" + deletedFlag);
            List<ApiRoleFormMap> list = apiRoleMapper.findByWhere(apiRoleFormMap);
            String[] roleName = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                roleName[i] = list.get(i).get("roleName").toString();
            }
            responseDto.setErrcode("0");
            responseDto.setErrmsg("查询所有角色成功！");
            responseDto.setResponseObject(roleName);
            return JsonUtils.bean2json(responseDto);
        } catch (Exception e) {
            e.getStackTrace();
            logger.error("查询所有角色异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("查询所有角色异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 获取系统参数
     * 
     * @param accessToken
     * @param key
     * @return
     */
    @Override
    public String getSystemSetting(String accessToken, String sysKey) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            // if (!checkAccessToken(accessToken, responseDto)) {
            // return JsonUtils.bean2json(responseDto);
            // }

            // 整合性check
            if (!checkSysKey(sysKey, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            List<ApiSystemSettingFormMap> lists = apiSystemSettingMapper.findByAttribute("SYS_KEY", sysKey,
                    ApiSystemSettingFormMap.class);
            if (lists == null || lists.isEmpty()) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg("系统参数不存在");
                return JsonUtils.bean2json(responseDto);
            }
            responseDto.setErrcode("0");// 校验成功
            responseDto.setErrmsg("查询系统参数成功");
            responseDto.setResponseObject(lists);
            return JsonUtils.bean2json(responseDto);
        } catch (Exception e) {
            e.getStackTrace();
            logger.error("查询系统参数异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("查询系统参数异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 修改系统参数
     * 
     * @param info
     * @return
     */
    @Override
    public String updateSystemSetting(String accessToken, String sysKey, SystemsettingInfo info) {
        ResponseDto responseDto = new ResponseDto();
        ApiSystemSettingFormMap apiSystemSettingFormMap = new ApiSystemSettingFormMap();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 数据整合性check
            if (!checkSystemParameterUpdate(info, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            if (!checkSystemIdByDB(sysKey, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 判断是否已被注册
            if (checkSystemColumn(info.getSysKey(), info.getId(), responseDto)) {
                apiSystemSettingFormMap.put("id", responseDto.getResponseObject());
                apiSystemSettingFormMap.putAll(BeanUtil.bean2Map(info));
                apiSystemSettingMapper.editEntity(apiSystemSettingFormMap);
                responseDto.setErrcode("0");// 校验成功
                responseDto.setErrmsg("更新系统参数成功！");
                responseDto.setResponseObject(apiSystemSettingFormMap);
            }
        } catch (Exception e) {
            e.getStackTrace();
            logger.error("更新系统参数异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("更新系统参数异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 删除系统参数
     * 
     * @param info
     * @param accessToken
     * @return
     */
    @Override
    public String deleteSystemSetting(String accessToken, String sysKey) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            // if (!checkAccessToken(accessToken, responseDto)) {
            // return JsonUtils.bean2json(responseDto);
            // }

            // 整合性check
            if (!checkSysKey(sysKey, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }

            // 指定系统参数数据存在check
            if (!checkSysKeyByDB(sysKey, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            ApiSystemSettingFormMap apiSystemSettingFormMap = new ApiSystemSettingFormMap();
            apiSystemSettingFormMap.put("id", responseDto.getResponseObject());
            apiSystemSettingFormMap.put("sysKey", sysKey);
            apiSystemSettingFormMap.put("deletedFlag", "1");
            apiSystemSettingMapper.editEntity(apiSystemSettingFormMap);
            responseDto.setErrcode("0");// 校验成功
            responseDto.setErrmsg("删除系统参数成功！");
            responseDto.setResponseObject(apiSystemSettingFormMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除系统参数异常");
            responseDto.setErrcode("1");
            responseDto.setErrmsg("删除系统参数异常");
            return JsonUtils.bean2json(responseDto);
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 验证accessToken不为空和存在
     * 
     * @param accessToken
     * @param responseDto
     * @return
     */
    private boolean checkAccessToken(String accessToken, ResponseDto responseDto) {
        // if (StringUtil.isEmptyString(accessToken)) {
        // responseDto.setErrcode("1");
        // responseDto.setErrmsg("校验关键字（ACCESS_TOKEN）不能为空");
        // return false;
        // }
        // List<ApiAccessTokenFormMap> lists =
        // apiAccessTokenMapper.findByAttribute("ACCESS_TOKEN", accessToken,
        // ApiAccessTokenFormMap.class);
        // if (lists == null || lists.size() == 0) {
        // responseDto.setErrcode("1");
        // responseDto.setErrmsg("权限过期，请重新登陆");
        // return false;
        // }
        return true;
    }

    /**
     * 添加bean没有的其他字段
     * 
     * @param apiUserFormMap
     */
    private void addOther(ApiUserFormMap apiUserFormMap) {
        //apiUserFormMap.put("enabledFlag", "0");
        apiUserFormMap.put("deleteFlag", "0");
        apiUserFormMap.put("enabledTime", DatetimeUtil.fromDateH());
        apiUserFormMap.put("deleteTime", DatetimeUtil.fromDateH());
        apiUserFormMap.put("createTime", DatetimeUtil.fromDateH());
        apiUserFormMap.put("updateTime", DatetimeUtil.fromDateH());
    }

    /**
     * 检查创建单个用户的参数
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkUserParameter(UserInfo userInfo, ResponseDto responseDto) {
        if (!checkUserInfo(userInfo, responseDto)) {
            return false;
        }

        if (!checkNamePassword(userInfo, responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * 验证用户名和密码不能为空
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkNamePassword(UserInfo userInfo, ResponseDto responseDto) {
        return this.checkNamePassword(userInfo, 0, responseDto);
    }

    /**
     * 验证用户名和密码不能为空
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkNamePassword(UserInfo userInfo, int i, ResponseDto responseDto) {
        if (StringUtil.isEmptyString(userInfo.getUserName())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + "用户名为空");
            return false;
        }
        if (StringUtil.isEmptyString(userInfo.getPassword())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + "密码为空");
            return false;
        }
        return true;
    }

    /**
     * 验证传进来的用户信息不为空
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkUserInfo(UserInfo userInfo, ResponseDto responseDto) {
        return this.checkUserInfo(userInfo, 0, responseDto);
    }

    /**
     * 验证传进来的用户信息不为空
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkUserInfo(UserInfo userInfo, int i, ResponseDto responseDto) {
        if (userInfo == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + "用户信息为空");
            return false;
        }

        return true;
    }

    /**
     * 检查批量创建用户的参数
     * 
     * @param accessToken
     * @param userList
     * @param responseDto
     * @return
     */
    private boolean checkUserList(List<UserInfo> userList, ResponseDto responseDto) {
        for (int i = 0; i < userList.size(); i++) {
            if (!checkUserInfo(userList.get(i), i + 1, responseDto)) {
                return false;
            }
            if (!checkNamePassword(userList.get(i), i + 1, responseDto)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 验证更新时部分参数唯一
     * 
     * @param column
     * @param columnC
     * @param columnE
     * @param userId
     * @param responseDto
     * @return
     */
    private boolean checkUserColumn(String columnValue, String columnDisc, String columnName, ResponseDto responseDto) {
        return this.checkUserColumn(columnValue, columnDisc, columnName, 0, responseDto);
    }

    /**
     * 验证更新时部分参数唯一
     * 
     * @param column
     * @param columnC
     * @param columnE
     * @param userId
     * @param responseDto
     * @return
     */
    private boolean checkUserColumn(String columnValue, String columnDisc, String columnName, int i,
            ResponseDto responseDto) {
        List<ApiUserFormMap> userInfos = apiUserMapper.findByAttribute(columnName, columnValue, ApiUserFormMap.class);
        if (userInfos != null && userInfos.size() > 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + columnDisc + "（" + columnValue + "）已被使用");
            return false;
        }
        return true;
    }

    /**
     * 验证更新时部分参数唯一
     * 
     * @param column
     * @param columnC
     * @param columnE
     * @param userId
     * @param responseDto
     * @return
     */
    private boolean checkUserColumn(String columnValue, String columnDisc, String columnName, String userName,
            ResponseDto responseDto) {
        return this.checkUserColumn(columnValue, columnDisc, columnName, userName, 0, responseDto);
    }

    /**
     * 验证更新时部分参数唯一
     * 
     * @param column
     * @param columnC
     * @param columnE
     * @param userId
     * @param responseDto
     * @return
     */
    private boolean checkUserColumn(String columnValue, String columnDisc, String columnName, String userName, int i,
            ResponseDto responseDto) {
        List<ApiUserFormMap> userInfos = apiUserMapper.findByAttribute(columnName, columnValue, ApiUserFormMap.class);
        if (userInfos != null && userInfos.size() > 0) {
            if (userName.equals(userInfos.get(0).get("userName").toString())) {
                return true;
            }
            responseDto.setErrcode("1");
            responseDto.setErrmsg((i == 0 ? "" : ("第" + i + "个")) + columnDisc + "（" + columnValue + "）已被使用");
            return false;
        }
        return true;
    }

    /**
     * 更新用户信息的参数检查
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkUserParameterUpdate(UserInfo userInfo, ResponseDto responseDto) {
        if (!checkUserParameter(userInfo, responseDto)) {
            return false;
        }
        if (!checkUserId(userInfo.getUserName(), responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * 验证更新时userId存在
     * 
     * @param userInfo
     * @param responseDto
     * @return
     */
    private boolean checkUserIdByDB(String userName, ResponseDto responseDto) {
        if (userName == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择用户");
            return false;
        }
        List<ApiUserFormMap> lists = apiUserMapper.findByAttribute("USER_NAME", userName, ApiUserFormMap.class);
        if (lists == null || lists.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("你选择的用户不存在");
            return false;
        }
        responseDto.setResponseObject(lists.get(0).get("userId").toString());
        return true;
    }

    /**
     * 删除用户的参数检查
     * 
     * @param accessToken
     * @param userName
     * @param responseDto
     * @return
     */
    private boolean checkUserName(String userName, ResponseDto responseDto) {
        return this.checkUserName(userName, 0, responseDto);
    }

    /**
     * 删除用户的参数检查
     * 
     * @param accessToken
     * @param userName
     * @param responseDto
     * @return
     */
    private boolean checkUserName(String userName, int i, ResponseDto responseDto) {
        if (StringUtil.isEmptyString(userName)) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择" + (i == 0 ? "" : "第" + i + "个") + "用户");
            return false;
        }
        return true;
    }

    /**
     * 删除用户的参数检查
     * 
     * @param accessToken
     * @param userName
     * @param responseDto
     * @return
     */
    private boolean checkUserId(String userName, ResponseDto responseDto) {
        return this.checkUserId(userName, 0, responseDto);
    }

    /**
     * 删除用户的参数检查
     * 
     * @param accessToken
     * @param userName
     * @param responseDto
     * @return
     */
    private boolean checkUserId(String userName, int i, ResponseDto responseDto) {
        if (userName == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择" + (i == 0 ? "" : "第" + i + "个") + "用户");
            return false;
        }
        return true;
    }

    /**
     * 检查用户是否存在
     * 
     * @param userName
     * @param i
     * @param responseDto
     * @return
     */
    private boolean checkUserNameByDB(String userName, ResponseDto responseDto) {
        List<ApiUserFormMap> userInfos = apiUserMapper.findByAttribute("USER_NAME", userName, ApiUserFormMap.class);// 用户信息查询
        if (userInfos == null || userInfos.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("用户(" + userName + ")不存在");
            return false;
        }
        String userId = userInfos.get(0).get("userId").toString();
        responseDto.setResponseObject(userId);
        return true;
    }

    /**
     * 批量删除用户信息参数验证
     * 
     * @param accessToken
     * @param userList
     * @param responseDto
     * @return
     */
    private boolean checkUserNameList(List<String> userNameList, ResponseDto responseDto) {
        if (userNameList == null || userNameList.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择用户");
            return false;
        }
        for (int i = 0; i < userNameList.size(); i++) {
            if (!this.checkUserName(userNameList.get(i), i, responseDto)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 批量删除用户信息参数验证
     * 
     * @param accessToken
     * @param userList
     * @param responseDto
     * @return
     */
    private boolean checkUserNameListByDB(List<String> userNameList, List<String> listParam, ResponseDto responseDto) {
        ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
        apiUserFormMap.put("userName", userNameList);
        List<ApiUserFormMap> userInfos = apiUserMapper.findByNames(apiUserFormMap);// 用户信息查询
        if (userInfos == null || userInfos.size() == 0 || userInfos.size() != userNameList.size()) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("用户有不存在的情况！");
            return false;
        }
        ApiUserFormMap apiUserFormMaps = new ApiUserFormMap();
        List<ApiUserFormMap> userInfo = apiUserMapper.findByNames(apiUserFormMaps);
        for (int i = 0; i < userNameList.size(); i++) {
            boolean flag = false;
            for (int j = 0; j < userInfo.size(); j++) {
                if (userInfo.get(j).get("userName").equals(userNameList.get(i))) {
                    flag = true;
                }
            }
            if (!flag) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg("第" + (i + 1) + "个用户(" + userNameList.get(i) + ")不存在");
                return false;
            }
            listParam.add(userInfos.get(i).get("userId").toString());
        }
        return true;
    }

    /**
     * 通过用户名获取角色ID
     * 
     * @param userName
     * @param responseDto
     * @return
     */
    private String[] getRoleIdByUserName(String userName, ResponseDto responseDto) {
        List<ApiUserFormMap> users = apiUserMapper.findByAttribute("USER_NAME", userName, ApiUserFormMap.class);// 用户信息查询
        if (users == null || users.size() == 0)
            return null;

        Object userId = users.get(0).get("userId");
        List<ApiUserRoleFormMap> userRoles = apiUserRoleMapper.findByAttribute("USER_ID", userId.toString(),
                ApiUserRoleFormMap.class);
        if (userRoles == null || userRoles.size() == 0) {
            return null;
        }
        String[] ids = new String[userRoles.size()];
        String[] roleName = new String[userRoles.size()];
        for (int i = 0; i < userRoles.size(); i++) {
            ids[i] = userRoles.get(i).get("roleId").toString();
            roleName[i] = apiRoleMapper.findByAttribute("ROLE_ID", ids[i], ApiRoleFormMap.class).get(0).get("roleName")
                    .toString();
        }
        return roleName;
    }

    /**
     * 获取角色用户信息参数验证
     * 
     * @param roleName
     * @param responseDto
     * @return
     */
    private boolean checkRoleName(String roleKey, ResponseDto responseDto) {
        return this.checkRoleName(roleKey, 0, responseDto);
    }

    /**
     * 获取角色用户信息参数验证
     * 
     * @param accessToken
     * @param roleName
     * @param responseDto
     * @return
     */
    private boolean checkRoleName(String roleKey, int i, ResponseDto responseDto) {
        if (StringUtil.isEmptyString(roleKey)) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择" + (i == 0 ? "" : "第" + i + "个") + "角色");
            return false;
        }
        return true;
    }

    /**
     * 通过角色名获取用户名
     * 
     * @param roleName
     * @param responseDto
     * @return
     */
    private String[] getUserIdByRoleName(String roleName, ResponseDto responseDto) {
        List<ApiRoleFormMap> roles = apiRoleMapper.findByAttribute("ROLE_NAME", roleName, ApiRoleFormMap.class);
        if (roles == null || roles.size() == 0) {
            return null;
        }
        Object roleId = roles.get(0).get("roleId");
        List<ApiUserRoleFormMap> userRoles = apiUserRoleMapper.findByAttribute("ROLE_ID", roleId.toString(),
                ApiUserRoleFormMap.class);
        if (userRoles == null || userRoles.size() == 0) {
            return null;
        }
        String[] userName = new String[userRoles.size()];
        for (int i = 0; i < userRoles.size(); i++) {
            ApiUserRoleFormMap info = userRoles.get(i);
            String userId = info.get("userId").toString();
            List<ApiUserFormMap> list = apiUserMapper.findByAttribute("USER_ID", userId, ApiUserFormMap.class);
            userName[i] = list.get(0).get("userName").toString();
        }
        return userName;
    }

    /**
     * 查询用户是否拥有该角色
     * 
     * @param user
     * @param role
     * @param responseDto
     * @return
     */
    private boolean checkUserIdRoleId(String userId, String roleId, ResponseDto responseDto) {
        return this.checkUserIdRoleId(userId, roleId, 0, responseDto);
    }

    /**
     * 查询用户是否拥有该角色
     * 
     * @param user
     * @param role
     * @param responseDto
     * @return
     */
    private boolean checkUserIdRoleId(String userId, String roleId, int i, ResponseDto responseDto) {
        ApiUserRoleFormMap apiUserRoleFormMap = new ApiUserRoleFormMap();
        apiUserRoleFormMap.put("userId", userId);
        apiUserRoleFormMap.put("roleId", roleId);
        List<ApiUserRoleFormMap> userRoles = apiUserRoleMapper.findByNames(apiUserRoleFormMap);
        if (userRoles != null && userRoles.size() > 0) {
            return false;
        }
        responseDto.setErrcode("1");// 校验失败
        responseDto.setErrmsg("用户(" + userId + ")未拥有" + (i == 0 ? "" : "第" + i + "个") + "角色(" + roleId + ")");
        return true;
    }

    /**
     * 查询用户是否拥有该角色
     * 
     * @param user
     * @param role
     * @param responseDto
     * @return
     */
    private boolean checkUserIdRoleIdForDel(String userId, String roleId, ResponseDto responseDto) {
        return this.checkUserIdRoleIdForDel(userId, roleId, 0, responseDto);
    }

    /**
     * 查询用户是否拥有该角色
     * 
     * @param user
     * @param role
     * @param responseDto
     * @return
     */
    private boolean checkUserIdRoleIdForDel(String userId, String roleId, int i, ResponseDto responseDto) {
        ApiUserRoleFormMap apiUserRoleFormMap = new ApiUserRoleFormMap();
        apiUserRoleFormMap.put("userId", userId);
        apiUserRoleFormMap.put("roleId", roleId);
        List<ApiUserRoleFormMap> userRoles = apiUserRoleMapper.findByNames(apiUserRoleFormMap);
        if (userRoles == null || userRoles.size() == 0) {
            responseDto.setErrcode("1");// 校验失败
            responseDto.setErrmsg("用户(" + userId + ")不拥有" + (i == 0 ? "" : "第" + i + "个") + "角色(" + roleId + ")");
            return false;
        }
        return true;
    }

    /**
     * 查询用户名是否存在
     * 
     * @param userName
     * @param responseDto
     * @return
     */
    private ApiUserFormMap getUserInfoByUserName(String userName, ResponseDto responseDto) {
        return this.getUserInfoByUserName(userName, 0, responseDto);
    }

    /**
     * 查询用户名是否存在
     * 
     * @param userName
     * @param responseDto
     * @return
     */
    private ApiUserFormMap getUserInfoByUserName(String userName, int i, ResponseDto responseDto) {
        List<ApiUserFormMap> users = apiUserMapper.findByAttribute("USER_NAME", userName, ApiUserFormMap.class);// 用户信息查询
        if (users == null || users.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg((i == 0 ? "" : "第" + i + "个") + userName + "用户不存在");
            return null;
        }
        return users.get(0);
    }

    /**
     * 查询角色名是否存在
     * 
     * @param roleName
     * @param responseDto
     * @return
     */
    private ApiRoleFormMap getRoleInfoByRoleName(String roleName, ResponseDto responseDto) {
        return this.getRoleInfoByRoleName(roleName, 0, responseDto);
    }

    /**
     * 查询角色名是否存在
     * 
     * @param roleName
     * @param responseDto
     * @return
     */
    private ApiRoleFormMap getRoleInfoByRoleName(String roleName, int i, ResponseDto responseDto) {
        List<ApiRoleFormMap> roles = apiRoleMapper.findByAttribute("ROLE_NAME", roleName, ApiRoleFormMap.class);// 用户信息查询
        if (roles == null || roles.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg((i == 0 ? "" : "第" + i + "个") + roleName + "角色不存在");
            return null;
        }
        return roles.get(0);
    }

    /**
     * 用户分配角色参数验证
     * 
     * @param accessToken
     * @param userName
     * @param roleName
     * @param responseDto
     * @return
     */
    private boolean checkUserNameRoleName(String userName, String roleName, ResponseDto responseDto) {
        if (!this.checkUserName(userName, responseDto)) {
            return false;
        }
        if (!this.checkRoleName(roleName, responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * 用户分配角色参数验证
     * 
     * @param accessToken
     * @param userName
     * @param roleName
     * @param responseDto
     * @return
     */
    private boolean checkUserNameRoleNameList(String userName, List<String> roleList, ResponseDto responseDto) {
        if (!this.checkUserName(userName, responseDto)) {
            return false;
        }
        if (roleList == null || roleList.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择角色");
            return false;
        }
        for (int i = 0; i < roleList.size(); i++) {
            if (!this.checkRoleName(roleList.get(i), i + 1, responseDto)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 批量分配时验证用户是否拥有该角色
     * 
     * @param userName
     * @param roleList
     * @param responseDto
     * @return
     */
    private List<ApiUserRoleFormMap> getUserIdRoleIdList(String userName, List<String> roleList,
            ResponseDto responseDto) {
        List<ApiUserRoleFormMap> apiUserRoleFormMaps = new ArrayList<ApiUserRoleFormMap>();
        List<ApiUserFormMap> apiUserFormMaps = apiUserMapper.findByAttribute("USER_NAME", userName,
                ApiUserFormMap.class);
        if (apiUserFormMaps == null || apiUserFormMaps.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg(userName + "用户不存在");
            return null;
        }
        for (int i = 0; i < roleList.size(); i++) {
            ApiRoleFormMap roleInfo = this.getRoleInfoByRoleName(roleList.get(i), i + 1, responseDto);
            if (roleInfo == null) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg("角色" + roleList.get(i) + "不存在");
                return null;
            }
            ApiUserRoleFormMap apiUserRoleFormMap = new ApiUserRoleFormMap();

            // 做用户角色关系存在check
            if (checkUserIdRoleId(apiUserFormMaps.get(0).get("userId").toString(), roleInfo.get("roleId").toString(),
                    responseDto)) {
                apiUserRoleFormMap.put("userId", apiUserFormMaps.get(0).get("userId").toString());
                apiUserRoleFormMap.put("roleId", roleInfo.get("roleId"));
                apiUserRoleFormMaps.add(apiUserRoleFormMap);
            } else {
                responseDto.setErrcode("1");
                responseDto.setErrmsg("用户" + userName + "已拥有角色" + roleList.get(i));
                return null;
            }
        }
        return apiUserRoleFormMaps;
    }

    /**
     * 批量分配时验证用户是否拥有该角色
     * 
     * @param userName
     * @param roleList
     * @param responseDto
     * @return
     */
    private List<ApiUserRoleFormMap> getUserIdRoleIdListForDel(String userId, List<String> roleList,
            ResponseDto responseDto) {
        List<ApiUserRoleFormMap> apiUserRoleFormMaps = new ArrayList<ApiUserRoleFormMap>();
        for (int i = 0; i < roleList.size(); i++) {
            ApiRoleFormMap roleInfo = this.getRoleInfoByRoleName(roleList.get(i), i + 1, responseDto);
            ApiUserRoleFormMap apiUserRoleFormMap = new ApiUserRoleFormMap();
            // 做用户角色关系存在check
            if (!checkUserIdRoleId(userId, roleInfo.get("roleId").toString(), responseDto)) {
                apiUserRoleFormMap.put("userId", userId);
                apiUserRoleFormMap.put("roleId", roleInfo.get("roleId"));
                apiUserRoleFormMaps.add(apiUserRoleFormMap);
            } else {
                return null;
            }
        }
        return apiUserRoleFormMaps;
    }

    /**
     * 添加javabean没有的其他字段
     * 
     * @param apiRoleFormMap
     */
    private void addOther(ApiRoleFormMap apiRoleFormMap, ResponseDto responseDto) {
        apiRoleFormMap.put("deletedFlag", "0");
        apiRoleFormMap.put("createTime", DatetimeUtil.fromDateH());
        apiRoleFormMap.put("updateTime", DatetimeUtil.fromDateH());
    }

    /**
     * 添加bean没有的其他字段
     * 
     * @param apiSystemSettingFormMap
     */
    private void addOther(ApiSystemSettingFormMap apiSystemSettingFormMap, ResponseDto responseDto) {
        apiSystemSettingFormMap.put("deletedFlag", "0");
        apiSystemSettingFormMap.put("createTime", DatetimeUtil.fromDateH());
    }

    /**
     * 检查创建单个角色的参数
     * 
     * @param roleInfo
     * @param responseDto
     * @return
     */
    private boolean checkRoleParameter(RoleInfo roleInfo, ResponseDto responseDto) {
        if (!checkRoleNameRoleDesc(roleInfo, responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * 验证角色名和角色描述不能为空
     * 
     * @param roleInfo
     * @param responseDto
     * @return
     */
    private boolean checkRoleNameRoleDesc(RoleInfo roleInfo, ResponseDto responseDto) {
        if (StringUtil.isEmptyString(roleInfo.getRoleName())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg("角色名为空");
            return false;
        }
        if (StringUtil.isEmpty(roleInfo.getRoleKey())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg("角色Key为空");
            return false;
        }
        if (StringUtil.isEmptyString(roleInfo.getComment())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg("角色描述为空");
            return false;
        }
        if (StringUtil.isEmptyString(roleInfo.getApplicationId())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg("applicationId为空");
            return false;
        }
        return true;
    }

    /**
     * 保存会话信息
     * @param session
     * @return
     * @throws Exception
     */
    private ApiAccessTokenFormMap saveAccessToken(HttpSession session) throws Exception {
        ApiAccessTokenFormMap apiAccessTokenFormMap = new ApiAccessTokenFormMap();
        apiAccessTokenFormMap.put("accessToken", UUID.randomUUID().toString());
        apiAccessTokenFormMap.put("sessionId", session.getId());
        apiAccessTokenFormMap.put("expiresIn", 1800);
        apiAccessTokenFormMap.put("updateTime", DatetimeUtil.getDate());
        apiAccessTokenMapper.addEntity(apiAccessTokenFormMap);
        return apiAccessTokenFormMap;
    }
//
//    /**
//     * 登录验证
//     * 
//     * @param userInfos
//     * @param userName
//     * @param password
//     * @param responseDto
//     * @return
//     */
//    private boolean checkUser(String userName, String password, ResponseDto responseDto) {
//        if (!checkUserNameByDB(userName, responseDto)) {
//            return false;
//        }
//        List<ApiUserFormMap> userInfos = apiUserMapper.findByAttribute("USER_NAME", userName, ApiUserFormMap.class);
//        if (!encrypt.decoder(userInfos.get(0).get("password").toString()).equals(password)) {// 密码解密
//            responseDto.setErrcode("1");
//            responseDto.setErrmsg("密码错误");
//            return false;
//        }
//        responseDto.setErrcode("0");
//        responseDto.setResponseObject(userInfos.get(0).get("userId"));
//        return true;
//    }

    /**
     * 验证创建时部分参数唯一
     * 
     * @param roleName
     * @param responseDto
     * @return
     */
    private boolean checkRoleColumn(String roleKey, ResponseDto responseDto) {
        List<ApiRoleFormMap> roleInfos = apiRoleMapper.findByAttribute("ROLE_KEY", roleKey, ApiRoleFormMap.class);
        if (roleInfos != null && roleInfos.size() > 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("角色Key已被使用");
            return false;
        }
        return true;
    }

    /**
     * 验证更新时部分参数唯一
     * 
     * @param roleName
     * @param roleId
     * @param responseDto
     * @return
     */
    private boolean checkRoleColumn(String roleKey, RoleInfo roleInfo, ResponseDto responseDto) {
        List<ApiRoleFormMap> role = apiRoleMapper.findByAttribute("ROLE_KEY", roleInfo.getRoleKey(),
                ApiRoleFormMap.class);
        if (role != null && role.size() > 0) {
            if (roleKey.equals(roleInfo.getRoleKey())) {
                return true;
            }
            responseDto.setErrcode("1");
            responseDto.setErrmsg("角色KEY已被使用");
            return false;
        }
        return true;
    }

    /**
     * 验证更新时部分参数唯一
     * 
     * @param sysKey
     * @param id
     * @param responseDto
     * @return
     */
    private boolean checkSystemColumn(String sysKey, int id, ResponseDto responseDto) {
        List<ApiSystemSettingFormMap> info = apiSystemSettingMapper.findByAttribute("SYS_KEY", sysKey,
                ApiSystemSettingFormMap.class);
        if (info != null && info.size() > 0) {
            if (info.get(0).get("id").equals(id)) {
                return true;
            }
            responseDto.setErrcode("1");
            responseDto.setErrmsg("sysKey已被使用");
            return false;
        }
        return true;
    }

    /**
     * 更新角色信息的参数检查
     * 
     * @param roleInfo
     * @param responseDto
     * @return
     */
    private boolean checkRoleParameterUpdate(RoleInfo roleInfo, ResponseDto responseDto) {
        if (!checkRoleParameter(roleInfo, responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * 验证更新时roleId存在
     * 
     * @param roleInfo
     * @param responseDto
     * @return
     */
    private boolean checkRoleIdByDB(String roleKey, ResponseDto responseDto) {
        if (roleKey == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择一个角色");
            return false;
        }
        List<ApiRoleFormMap> lists = apiRoleMapper.findByAttribute("ROLE_KEY", roleKey, ApiRoleFormMap.class);
        if (lists == null || lists.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("你选择的角色不存在");
            return false;
        }
        responseDto.setResponseObject(lists.get(0).get("roleId").toString());
        return true;
    }

    /**
     * 检查角色是否存在
     * 
     * @param roleName
     * @param i
     * @param responseDto
     * @return
     */
    private boolean checkRoleNameByDB(String roleKey, ResponseDto responseDto) {
        List<ApiRoleFormMap> roleInfo = apiRoleMapper.findByAttribute("ROLE_KEY", roleKey, ApiRoleFormMap.class);// 用户信息查询
        if (roleInfo == null || roleInfo.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("角色key(" + roleKey + ")不存在");
            return false;
        }
        responseDto.setResponseObject(roleInfo.get(0).get("roleId").toString());
        return true;
    }

    /**
     * 验证创建时部分参数唯一
     * 
     * @param sysKey
     * @param responseDto
     * @return
     */
    private boolean checkSystemColumn(String sysKey, ResponseDto responseDto) {
        List<ApiSystemSettingFormMap> info = apiSystemSettingMapper.findByAttribute("SYS_KEY", sysKey,
                ApiSystemSettingFormMap.class);
        if (info != null && info.size() > 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("系统参数sysKey已被使用");
            return false;
        }
        return true;
    }

    /**
     * 检查创建单个系统参数的参数
     * 
     * @param info
     * @param responseDto
     * @return
     */
    private boolean checkSystemParameter(SystemsettingInfo info, ResponseDto responseDto) {
        if (!checkSysKeySysValue(info, responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * 验证系统参数syskey和sysValue不能为空
     * 
     * @param info
     * @param responseDto
     * @return
     */
    private boolean checkSysKeySysValue(SystemsettingInfo info, ResponseDto responseDto) {
        if (StringUtil.isEmptyString(info.getSysKey())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg("系统参数sysKey为空");
            return false;
        }

        if (StringUtil.isEmptyString(info.getSysValue())) {
            responseDto.setErrcode("1");//
            responseDto.setErrmsg("系统参数sysValue为空");
            return false;
        }
        return true;
    }

    /**
     * 更新系统参数信息的参数检查
     * 
     * @param info
     * @param responseDto
     * @return
     */
    private boolean checkSystemParameterUpdate(SystemsettingInfo info, ResponseDto responseDto) {
        if (!checkSystemParameter(info, responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * 验证更新时Id存在
     * 
     * @param info
     * @param responseDto
     * @return
     */
    private boolean checkSystemIdByDB(String sysKey, ResponseDto responseDto) {
        if (sysKey == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择一个系统参数");
            return false;
        }
        List<ApiSystemSettingFormMap> lists = apiSystemSettingMapper.findByAttribute("SYS_KEY", sysKey,
                ApiSystemSettingFormMap.class);
        if (lists == null || lists.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("你选择的系统参数不存在");
            return false;
        }
        responseDto.setResponseObject(lists.get(0).get("id").toString());
        return true;
    }

    /**
     * 删除系统参数的参数检查
     * 
     * @param sysKey
     * @param responseDto
     * @return
     */
    private boolean checkSysKey(String sysKey, ResponseDto responseDto) {
        if (StringUtil.isEmptyString(sysKey)) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("系统参数sysKey为空");
            return false;
        }
        return true;
    }

    /**
     * 检查系统参数是否存在
     * 
     * @param sysKey
     * @param i
     * @param responseDto
     * @return
     */
    private boolean checkSysKeyByDB(String sysKey, ResponseDto responseDto) {
        List<ApiSystemSettingFormMap> info = apiSystemSettingMapper.findByAttribute("SYS_KEY", sysKey,
                ApiSystemSettingFormMap.class);// 用户信息查询
        if (info == null || info.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("系统参数(" + sysKey + ")不存在");
            return false;
        }
        responseDto.setResponseObject(info.get(0).get("id").toString());
        return true;
    }

    /**
     * 创建部门
     * 
     * @param info
     * @return
     */
    @Override
    public String createDept(String accessToken, DeptmentInfo info) {
        ResponseDto responseDto = new ResponseDto();
        SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 数据整合性check
            if (!checkDeptMentParameter(info, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            // 判断是否注册
            if (checkDeptCloumn(info.getDeptCode(), responseDto)) {
                sysDepartmentFormMap.put("id", UUID.randomUUID().toString());
                sysDepartmentFormMap.putAll(BeanUtil.bean2Map(info));
                addOther(sysDepartmentFormMap, responseDto);
                sysDepartmentMapper.addEntity(sysDepartmentFormMap);
                responseDto.setErrcode("0");
                responseDto.setErrmsg("创建部门成功");
                responseDto.setResponseObject(sysDepartmentFormMap);
            }
        } catch (Exception e) {
            e.getStackTrace();
            responseDto.setErrcode("1");
            responseDto.setErrmsg("部门创建异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 添加其他字段
     * 
     * @param sysDepartmentFormMap
     * @param responseDto
     */
    private void addOther(SysDepartmentFormMap sysDepartmentFormMap, ResponseDto responseDto) {
        sysDepartmentFormMap.put("status", "0");
    }

    /**
     * 验证创建时部分参数唯一
     * 
     * @param deptCode
     * @param responseDto
     * @return
     */
    private boolean checkDeptCloumn(String deptCode, ResponseDto responseDto) {
        List<SysDepartmentFormMap> deptInfo = sysDepartmentMapper.findByAttribute("DEPT_CODE", deptCode,
                SysDepartmentFormMap.class);
        if (deptInfo != null && deptInfo.size() > 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("部门编码已被使用");
            return false;
        }
        return true;
    }

    /**
     * 检查创建单个部门的参数
     * 
     * @param info
     * @return
     */
    private boolean checkDeptMentParameter(DeptmentInfo info, ResponseDto responseDto) {
        if (!checkDeptMentInfo(info, responseDto)) {
            return false;
        }
        return true;
    }

    /**
     * 验证部门编码 部门名称 上级部门ID 非空
     * 
     * @param info
     * @param responseDto
     * @return
     */
    private boolean checkDeptMentInfo(DeptmentInfo info, ResponseDto responseDto) {
        if (StringUtil.isEmpty(info.getParentId())) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("上级部门ID为空");
            return false;
        }
        if (StringUtil.isEmpty(info.getDeptCode())) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("部门编码为空");
            return false;
        }
        if (StringUtil.isEmpty(info.getDeptName())) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("部门名称为空");
            return false;
        }
        return true;
    }

    /**
     * 删除部门
     * 
     * @param deptCode
     * @return
     */
    @Override
    public String deleteDept(String accessToken, String deptCode) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            if (!checkDeptMentByDB(deptCode, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
            sysDepartmentFormMap.put("id", responseDto.getResponseObject());
            sysDepartmentFormMap.put("status", "1");
            sysDepartmentMapper.editEntity(sysDepartmentFormMap);
            responseDto.setErrcode("0");
            responseDto.setErrmsg("删除部门信息成功");
            responseDto.setResponseObject(sysDepartmentFormMap);
        } catch (Exception e) {
            e.getStackTrace();
            responseDto.setErrcode("1");
            responseDto.setErrmsg("删除部门信息异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 验证部门是否存在
     * 
     * @param deptCode
     * @param responseDto
     * @return
     */
    private boolean checkDeptMentByDB(String deptCode, ResponseDto responseDto) {
        List<SysDepartmentFormMap> deptInfo = sysDepartmentMapper.findByAttribute("DEPT_CODE", deptCode,
                SysDepartmentFormMap.class);
        if (deptInfo == null || deptInfo.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("部门编号（" + deptCode + "）对应的部门不存在");
            return false;
        }
        responseDto.setResponseObject(deptInfo.get(0).get("id").toString());
        return true;
    }

    @Override
    public String deleteDeptlist(String accessToken, List<String> deptCodelist) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            if (!checkdeptCodeList(deptCodelist, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            List<String> listparam = new ArrayList<String>();
            if (checkDeptCodeListByDB(deptCodelist, listparam, responseDto)) {
                for (String id : listparam) {
                    SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
                    sysDepartmentFormMap.put("id", id);
                    sysDepartmentFormMap.put("status", "1");
                    sysDepartmentMapper.editEntity(sysDepartmentFormMap);
                }
                responseDto.setErrcode("0");
                responseDto.setErrmsg("批量删除部门成功");
            }
        } catch (Exception e) {
            e.getStackTrace();
            responseDto.setErrcode("1");
            responseDto.setErrmsg("批量删除部门异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 批量删除部门信息参数验证
     * 
     * @param deptCodelist
     * @param listparam
     * @param responseDto
     * @return
     */
    private boolean checkDeptCodeListByDB(List<String> deptCodelist, List<String> listparam, ResponseDto responseDto) {
        SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
        sysDepartmentFormMap.put("DEPT_CODE", deptCodelist);
        List<SysDepartmentFormMap> deptInfos = sysDepartmentMapper.findByNames(sysDepartmentFormMap);
        if (deptInfos == null || deptInfos.size() == 0 || deptInfos.size() != deptCodelist.size()) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("部门有不存在的情况");
            return false;
        }
        SysDepartmentFormMap sysDepartmentFormMaps = new SysDepartmentFormMap();
        List<SysDepartmentFormMap> depts = sysDepartmentMapper.findByNames(sysDepartmentFormMaps);
        for (int i = 0; i < deptCodelist.size(); i++) {
            boolean flag = false;
            for (int j = 0; j < depts.size(); j++) {
                if (depts.get(j).get("deptCode").equals(deptCodelist.get(i))) {
                    flag = true;
                }
            }
            if (!flag) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg("第" + (i + 1) + "个部门编号（" + deptCodelist.get(i) + "）对应的部门不存在");
                return false;
            }
            listparam.add(deptInfos.get(i).get("id").toString());
        }
        return true;
    }

    /**
     * 批量删除部门参数验证
     * 
     * @param deptCodelist
     * @param responseDto
     * @return
     */
    private boolean checkdeptCodeList(List<String> deptCodelist, ResponseDto responseDto) {
        if (deptCodelist == null || deptCodelist.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择部门");
            return false;
        }
        for (int i = 0; i < deptCodelist.size(); i++) {
            if (!checkdeptCode(deptCodelist.get(i), i, responseDto)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 批量删除用户的参数检查
     * 
     * @param string
     * @param i
     * @param responseDto
     * @return
     */
    private boolean checkdeptCode(String deptCode, int i, ResponseDto responseDto) {
        if (StringUtil.isEmpty(deptCode)) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择" + (i == 0 ? "" : "第" + i + "个") + "部门");
            return false;
        }
        return true;
    }

    /**
     * 获取特定部门的信息
     */
    @Override
    public String getDeptInfo(String accessToken,String deptCode) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            if (!checkDeptMentByDB(deptCode, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            List<SysDepartmentFormMap> list = sysDepartmentMapper.findByAttribute("DEPT_CODE", deptCode,
                    SysDepartmentFormMap.class);
            if (list == null || list.isEmpty()) {
                responseDto.setErrcode("1");
                responseDto.setErrmsg(deptCode + " 对应的部门不存在");
                return JsonUtils.bean2json(responseDto);
            }
            SysDepartmentFormMap sysDepartmentFormMap = list.get(0);
            if (!"0".equals(list.get(0).getStr("parentId"))) {
                List<SysDepartmentFormMap> parlist = sysDepartmentMapper.findByAttribute("ID", list.get(0).getStr("parentId"),
                        SysDepartmentFormMap.class);
                sysDepartmentFormMap.put("parentCode", parlist.get(0).getStr("deptCode"));
            }else {
                sysDepartmentFormMap.put("parentCode", "0");
            }
            responseDto.setErrcode("0");
            responseDto.setErrmsg("查询部门信息成功");
            responseDto.setResponseObject(sysDepartmentFormMap);
        } catch (Exception e) {
            e.getStackTrace();
            responseDto.setErrcode("1");
            responseDto.setErrmsg("查询部门信息异常");

        }
        return JsonUtils.bean2json(responseDto);
    }

    
    
    /**
     * 获取所有部门信息
     * 
     * @return
     */
    @Override
    public String getAllDeptInfo(String accessToken) {
        ResponseDto responseDto = new ResponseDto();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
            sysDepartmentFormMap.put("where", "where STATUS='0'");
            List<SysDepartmentFormMap> list = sysDepartmentMapper.findByWhere(sysDepartmentFormMap);
            responseDto.setErrcode("0");
            responseDto.setErrmsg("查询所有部门信息成功");
            responseDto.setResponseObject(list);
        } catch (Exception e) {
            e.getStackTrace();
            responseDto.setErrcode("1");
            responseDto.setErrmsg("查询所有部门信息异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 更新部门信息
     * 
     * @param deptCode
     * @param info
     * @return
     */
    @Override
    public String updateDept(String accessToken, String deptCode, DeptmentInfo info) {
        ResponseDto responseDto = new ResponseDto();
        SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
        try {
            // access_token参数判断
            if (!checkAccessToken(accessToken, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            if (!checkDeptMentInfo(info, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            if (!checkDeptCode(deptCode, responseDto)) {
                return JsonUtils.bean2json(responseDto);
            }
            if (checkDeptCloumn(deptCode, info, responseDto)) {
                sysDepartmentFormMap.put("id", responseDto.getResponseObject());
                sysDepartmentFormMap.putAll(BeanUtil.bean2Map(info));
                sysDepartmentMapper.editEntity(sysDepartmentFormMap);
                responseDto.setErrcode("0");
                responseDto.setErrmsg("更新部门信息成功");
                responseDto.setResponseObject(sysDepartmentFormMap);
            }
        } catch (Exception e) {
            e.getStackTrace();
            responseDto.setErrcode("1");
            responseDto.setErrmsg("更新部门信息异常");
        }
        return JsonUtils.bean2json(responseDto);
    }

    /**
     * 验证更新时部分参数唯一
     * 
     * @param deptCode
     * @param info
     * @param responseDto
     * @return
     */
    private boolean checkDeptCloumn(String deptCode, DeptmentInfo info, ResponseDto responseDto) {
        List<SysDepartmentFormMap> list = sysDepartmentMapper.findByAttribute("DEPT_CODE", info.getDeptCode(),
                SysDepartmentFormMap.class);
        if (list != null && list.size() > 0) {
            if (deptCode.equals(info.getDeptCode())) {
                return true;
            }
            responseDto.setErrcode("1");
            responseDto.setErrmsg("部门编号已被使用");
            return false;
        }
        return true;
    }

    /**
     * 验证更新时部门是否存在
     * 
     * @param deptCode
     * @param responseDto
     * @return
     */
    private boolean checkDeptCode(String deptCode, ResponseDto responseDto) {
        if (deptCode == null) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("请选择一个部门");
            return false;
        }
        List<SysDepartmentFormMap> list = sysDepartmentMapper.findByAttribute("DEPT_CODE", deptCode,
                SysDepartmentFormMap.class);
        if (list == null || list.size() == 0) {
            responseDto.setErrcode("1");
            responseDto.setErrmsg("您选择的部门不存在可");
            return false;
        }
        responseDto.setResponseObject(list.get(0).get("id").toString());
        return true;
    }

}
