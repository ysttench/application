package com.ysttench.application.htbw.core.service.Impl;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.jws.WebService;

import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.common.util.encrypt.Encrypt;
import com.ysttench.application.htbw.core.rdto.Jzchange;
import com.ysttench.application.htbw.core.rdto.ResponseDto;
import com.ysttench.application.htbw.core.rdto.SocketClient;
import com.ysttench.application.htbw.core.service.AuthService;
import com.ysttench.application.htbw.settings.kernel.entity.AnnountFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.EquipmentFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.EquipmentMsgFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.LogRecordFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.LogUserLoginFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.SysUserFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.WarnMsgFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.WitchFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.AnnountMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.EquipmentMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.LogRecordMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.LogUserLoginMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.SysUserMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.WarnMsgMapper;
import com.ysttench.application.htbw.settings.web.listener.InitMapperListener;

/**
 * 接口实现
 * 
 * @author Howard
 * 
 */
@WebService(targetNamespace = "http://service.core.htbw.application.ysttench.com/", endpointInterface = "com.ysttench.application.htbw.core.service.AuthService")
public class AuthServiceImpl implements AuthService {
    @Inject
    private SysUserMapper sysUserMapper;
    @Inject
    private EquipmentMapper equipmentMapper;
    @Inject
    private WarnMsgMapper warnMsgMapper;
    @Inject
    private LogRecordMapper logRecordMapper;
    @Inject
    private AnnountMapper annountMapper;
    @Inject
    private LogUserLoginMapper logUserLoginMapper;
    Jzchange Jzchange = new Jzchange();
    // 密码加密
    private Encrypt encrypt = new Encrypt();
    public static List<SocketClient> socketlist = InitMapperListener.socketlist;

    @Override
    public String login(String userName, String password) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    SysUserFormMap sysUserFormMap = new SysUserFormMap();
	    sysUserFormMap.put("where", "where DELETE_STATUS='0' AND USER_NAME='" + userName + "'");
	    List<SysUserFormMap> mps = sysUserMapper.findByWhere(sysUserFormMap);
	    if (mps != null && mps.size() > 0) {
		SysUserFormMap tempUserFormMap = mps.get(0);
		if (tempUserFormMap.containsKey("password") && tempUserFormMap.getStr("password") != null) {
		    String dbPassword = encrypt.decoder(tempUserFormMap.getStr("password"));
		    if (dbPassword.equals(password)) {
			LogUserLoginFormMap logUserLoginFormMap = new LogUserLoginFormMap();
			logUserLoginFormMap.put("userId", tempUserFormMap.get("userId").toString());
			logUserLoginFormMap.put("userName", userName);
			logUserLoginFormMap.put("loginIp", SessionUtil.getIpAddress());
			logUserLoginMapper.addEntity(logUserLoginFormMap);
			responseDto.setErrcode("0");
			responseDto.setErrmsg("登陆成功");
		    } else {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("密码错误");
		    }
		}
	    } else {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("该用户不存在");
	    }

	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("登陆验证异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String updatepassword(String userName, String oldpassword, String newpassword) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    SysUserFormMap sysUserFormMap = new SysUserFormMap();
	    sysUserFormMap.put("where", "where DELETE_STATUS='0' AND USER_NAME='" + userName + "'");
	    List<SysUserFormMap> mps = sysUserMapper.findByWhere(sysUserFormMap);
	    if (mps != null && mps.size() > 0) {
		SysUserFormMap tempUserFormMap = mps.get(0);
		if (tempUserFormMap.containsKey("password") && tempUserFormMap.getStr("password") != null) {
		    String dbPassword = encrypt.decoder(tempUserFormMap.getStr("password"));
		    if (dbPassword.equals(oldpassword)) {
			SysUserFormMap map = new SysUserFormMap();
			map.put("userId", tempUserFormMap.get("userId").toString());
			map.put("userName", userName);
			map.put("password", encrypt.encoder(newpassword));
			sysUserMapper.editEntity(map);
			responseDto.setErrcode("0");
			responseDto.setErrmsg("改密成功");
		    } else {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("旧密码错误");
		    }
		}
	    } else {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("该用户不存在");
	    }
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrcode("登陆异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String getEquipmentMsg(String checkValue) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    EquipmentFormMap map = new EquipmentFormMap();
	    map.put("checkValue", checkValue);
	    List<EquipmentFormMap> list = equipmentMapper.findEquipPage(map);
	    if (list.size() == 0) {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("暂无信息");
	    } else {
		responseDto.setErrcode("0");
		responseDto.setErrmsg("获取信息成功");
		responseDto.setResponseObject(list);
	    }
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("查询异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String getWarnMsg(String checkValue, String time) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    WarnMsgFormMap warnMsgFormMap = new WarnMsgFormMap();
	    if (!StringUtil.isEmpty(time)) {
		warnMsgFormMap.put("time", time);
	    }
	    if (!StringUtil.isEmpty(checkValue)) {
		warnMsgFormMap.put("checkValue", checkValue);
	    }
	    List<WarnMsgFormMap> list = warnMsgMapper.findWarnMsgByPage(warnMsgFormMap);
	    if (list.size() == 0) {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("暂无信息");
	    } else {
		responseDto.setErrcode("0");
		responseDto.setErrmsg("获取信息成功");
		responseDto.setResponseObject(list);
	    }
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("查询异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String getlogRecord(String userName, String checkValue) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    LogRecordFormMap map = new LogRecordFormMap();
	    if (!StringUtil.isEmpty(checkValue)) {
		map.put("where", "where (LOG_RECORD.JB_DESC LIKE '%" + checkValue + "%') AND LOG_RECORD.JB_NAME= '"
			+ userName + "'");
	    } else {
		map.put("where", "where  LOG_RECORD.JB_NAME= '" + userName + "'");
	    }
	    List<LogRecordFormMap> list = logRecordMapper.findByWhere(map);
	    if (list.size() == 0) {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("暂无信息");
	    } else {
		responseDto.setErrcode("0");
		responseDto.setErrmsg("获取信息成功");
		responseDto.setResponseObject(list);
	    }
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("查询异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String getAnnouncement(String checkValue) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    AnnountFormMap map = new AnnountFormMap();
	    if (!StringUtil.isEmpty(checkValue)) {
		map.put("where", "where (ANNOUNMENT.FB_NAME LIKE '%" + checkValue + "%' OR ANNOUNMENT.GG_DESC LIKE '%"
			+ checkValue + "%' OR ANNOUNMENT.GG_MSG LIKE '%" + checkValue + "%')");
	    }
	    List<AnnountFormMap> list = annountMapper.findByWhere(map);
	    if (list.size() == 0) {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("暂无信息");
	    } else {
		responseDto.setErrcode("0");
		responseDto.setErrmsg("获取信息成功");
		responseDto.setResponseObject(list);
	    }
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("查询异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String addslogRecord(String userName) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    LogRecordFormMap map = new LogRecordFormMap();
	    map.put("jbName", userName);
	    map.put("sbDate", DatetimeUtil.getDate());
	    logRecordMapper.addEntity(map);
	    responseDto.setErrcode("0");
	    responseDto.setErrmsg("添加成功");
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("添加异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String addlogRecord(String recordId, String jbDesc) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    LogRecordFormMap map = new LogRecordFormMap();
	    map.put("id", recordId);
	    map.put("jbDesc", jbDesc);
	    map.put("jbDate", DatetimeUtil.getDate());
	    logRecordMapper.editEntity(map);
	    responseDto.setErrcode("0");
	    responseDto.setErrmsg("添加成功");
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("添加异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String monitor() {
	ResponseDto responseDto = new ResponseDto();
	try {
	    List<EquipmentMsgFormMap> list = equipmentMapper.findJkMsg(new EquipmentMsgFormMap());
	    responseDto.setErrcode("0");
	    responseDto.setErrmsg("获取信息成功");
	    responseDto.setResponseObject(list);
	    System.out.println(list);
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("获取信息异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String getwitchRecord() {
	ResponseDto responseDto = new ResponseDto();
	try {
	    List<WitchFormMap> list = equipmentMapper.getWitchMsg(new WitchFormMap());
	    if (list.size() == 0) {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("暂无信息");
	    } else {
		responseDto.setErrcode("0");
		responseDto.setErrmsg("获取信息成功");
		responseDto.setResponseObject(list);
	    }
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("获取信息异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String getbEquipmentMsg(String equipmentId) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    EquipmentMsgFormMap map = new EquipmentMsgFormMap();
	    map.put("equipmentId", equipmentId);
	    List<EquipmentMsgFormMap> list = equipmentMapper.findChangeCurv(map);
	    if (list.size() == 0) {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("暂无信息");
	    } else {
		responseDto.setErrcode("0");
		responseDto.setErrmsg("获取信息成功");
		responseDto.setResponseObject(list);
	    }
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("获取信息异常");
	}
	return JsonUtils.bean2json(responseDto);
    }
}