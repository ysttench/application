package com.ysttench.application.htbw.settings.web.controller.common;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.ysttench.application.center.core.service.AuthService;
import com.ysttench.application.common.server.CXFClientUtil;
import com.ysttench.application.htbw.settings.kernel.entity.SysSystemForMap;
import com.ysttench.application.htbw.settings.kernel.mapper.SysSystemMapper;

@Component
public class AuthConfigComponent {
    @Inject
    private SysSystemMapper sysSystemMapper;
    
    public AuthService getAuthService() {
	SysSystemForMap map = sysSystemMapper.findbyFrist("id", "1", SysSystemForMap.class);
	String  url = "http://" + map.getStr("centerIp") + ":" + map.getStr("centerPort")
		    + "/center/services/AuthService?wsdl";
        return CXFClientUtil.getInterfaceService(url, AuthService.class);
    }
}
