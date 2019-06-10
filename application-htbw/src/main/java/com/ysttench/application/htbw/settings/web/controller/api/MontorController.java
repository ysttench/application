package com.ysttench.application.htbw.settings.web.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;

@Controller
@RequestMapping("/montor/")
public class MontorController {
    /**
     * 跳转列表页面
     * 
     * @return
     */
    @RequestMapping("list")
    public String listUI() {
	return ForwardConstants.API + ForwardConstants.MONTOR + ForwardConstants.LIST;
    }

}
