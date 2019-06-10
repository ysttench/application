/*package com.ysttench.application.msd.settings.web.controller.system;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ysttench.application.center.settings.kernel.entity.ApiUserFormMap;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.util.encrypt.Encrypt;
import com.ysttench.application.msd.settings.kernel.entity.SysSystemForMap;
import com.ysttench.application.msd.settings.kernel.mapper.SysSystemMapper;
import com.ysttench.application.msd.settings.web.controller.common.AttrConstants;
import com.ysttench.application.msd.settings.web.controller.common.AuthConfigComponent;
import com.ysttench.application.msd.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.msd.settings.web.controller.index.BaseController;

*//**
 * 
 * @author Howard
 *//*
@Controller
@RequestMapping("/register/")
public class SysRegisterController extends BaseController {
	@Inject
	private AuthConfigComponent authConfigComponent;
	@Inject
	private SysSystemMapper sysSystemMapper;
	// 密码加密
    private Encrypt encrypt = new Encrypt();

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return ForwardConstants.REGISTER + ForwardConstants.ADD;
	}

	@ResponseBody
	@RequestMapping("add")
	@SystemLog(module = "系统管理", methods = "系统管理-系统激活") // 凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly = false) // 需要事务操作必须加入此注解
	public String addEntity() {
		ApiUserFormMap apiUserFormMap = getFormMap(ApiUserFormMap.class);
		String resultJson = authConfigComponent.getAuthService().register(apiUserFormMap);
		JSONObject jobj = JSONObject.parseObject(resultJson);
		if ("0".equals(jobj.getString("errcode"))) {
			JSONObject jobj1 = JSONObject.parseObject(jobj.getString("responseObject").toString());
			SysSystemForMap sysSystemForMap = new SysSystemForMap();
			sysSystemForMap.put("flag",encrypt.encoder(jobj1.get("activityDate").toString()+","+jobj1.get("registerDate").toString()) );
			sysSystemForMap.put("email", jobj1.get("email").toString());
			sysSystemForMap.put("id", 1);
			sysSystemMapper.editsystem(sysSystemForMap);
			return AttrConstants.SUCCESS;
		}
		return AttrConstants.FAIL;
	}

	*//**
	 * 注册验证
	 * 
	 * @param companyName
	 * @param email
	 * @param mobile
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("checkField")
	@ResponseBody
	public boolean checkField(String fieldName, String value) throws Exception {
		String resultJson = authConfigComponent.getAuthService().checkField(fieldName, value);
		JSONObject jobj = JSONObject.parseObject(resultJson);
		if (!"0".equals(jobj.getString("errcode"))) {
			return false;
		} else {
			return true;
		}
	}
}*/