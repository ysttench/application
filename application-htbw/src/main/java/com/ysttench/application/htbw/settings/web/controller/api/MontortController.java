package com.ysttench.application.htbw.settings.web.controller.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;

@Controller
@RequestMapping("/montort/")
public class MontortController {
    /**
     * 跳转列表页面
     * 
     * @return
     */
@RequestMapping("list")
public String listUI(Model model) {
	return ForwardConstants.API + ForwardConstants.MONTORT + ForwardConstants.LIST;
}

}
