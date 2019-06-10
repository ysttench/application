package com.ysttench.application.msd.settings.web.controller.common;

import org.springframework.stereotype.Component;

import com.ysttench.application.center.core.service.AuthService;
import com.ysttench.application.common.server.CXFClientUtil;


/**统一认证配置共通组件
 * @author Howard
 *
 */
@Component
public class AuthConfigComponent {

    /**
     * 获取统一认证接口类
     * @return
     */
    public AuthService getAuthService() {
        return CXFClientUtil.getInterfaceService("http://localhost:8008/center/services/AuthService?wsdl", AuthService.class);
    }



  
}