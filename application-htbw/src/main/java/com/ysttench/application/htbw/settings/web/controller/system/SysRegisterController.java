package com.ysttench.application.htbw.settings.web.controller.system;

import java.io.IOException;

import javax.inject.Inject;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSONObject;
import com.ysttench.application.center.settings.web.controller.common.AttrConstants;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.encrypt.Encrypt;
import com.ysttench.application.htbw.core.rdto.MacMsg;
import com.ysttench.application.htbw.settings.kernel.entity.SysSystemForMap;
import com.ysttench.application.htbw.settings.kernel.mapper.SysSystemMapper;
import com.ysttench.application.htbw.settings.web.controller.common.AuthConfigComponent;
import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htbw.settings.web.controller.index.BaseController;

/**
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/register/")
public class SysRegisterController extends BaseController {
    @Inject
    private SysSystemMapper sysSystemMapper;
    @Inject
	private AuthConfigComponent authConfigComponent;
    // 密码加密
    private Encrypt encrypt = new Encrypt();

    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
	return ForwardConstants.SYSTEM+ForwardConstants.REGISTER + ForwardConstants.ADD;
    }

    @ResponseBody
    @RequestMapping("add")
    @SystemLog(module = "系统管理", methods = "系统管理-系统激活") // 凡需要处理业务逻辑的.都需要记录操作日志
    @Transactional(readOnly = false) // 需要事务操作必须加入此注解
    public String addEntity() {
	try {
	    String macAddress = new MacMsg().getMacMsg();
	    SysSystemForMap map = getFormMap(SysSystemForMap.class);
	    String resultJson = authConfigComponent.getAuthService().register(map.getStr("companyName"),map.getStr("email"),map.getStr("mobile"),macAddress);
	    JSONObject jobj = JSONObject.parseObject(resultJson);
	    if ("0".equals(jobj.getString("errcode"))) {
		String flag = jobj.getString("responseObject").toString();
		map.put("flag", flag);
		map.put("id", "1");
		map.put("activDate", DatetimeUtil.getDate());
		sysSystemMapper.editEntity(map);
		return AttrConstants.SUCCESS;
	    }
	} catch (Exception e) {
	    return AttrConstants.FAIL1;
	}
	return AttrConstants.FAIL;
    }

    @RequestMapping("edit")
    public String editUI(Model model) throws Exception {
	return ForwardConstants.SYSTEM+ForwardConstants.REGISTER + ForwardConstants.EDIT;
    }
    @ResponseBody
    @RequestMapping("editEntity")
    @SystemLog(module = "系统管理", methods = "系统管理-系统激活") // 凡需要处理业务逻辑的.都需要记录操作日志
    @Transactional(readOnly = false) // 需要事务操作必须加入此注解
    public String editEntity() {
	SysSystemForMap map = getFormMap(SysSystemForMap.class);
	map.put("id", "1");
	try {
	    map.put("activDate", DatetimeUtil.getDate());
	    sysSystemMapper.editEntity(map);
	} catch (Exception e) {
	    e.printStackTrace();
	    return AttrConstants.FAIL;
	}
	return AttrConstants.SUCCESS;
	
    }
}