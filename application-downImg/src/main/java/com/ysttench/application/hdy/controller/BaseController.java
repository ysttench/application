package com.ysttench.application.hdy.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ysttench.application.hdy.mapper.SysmsgMapper;
import com.ysttench.application.hdy.rdto.SysMsg;

@Controller
@RequestMapping("/")
public class BaseController {
    @Autowired
    SysmsgMapper sysmsgMapper;
    @RequestMapping(value = "")
    public String toIndex() {
	return ForwardConstants.REDIRECT+ForwardConstants.LOGIN;
    }
    @RequestMapping(value = "login", method = RequestMethod.GET, produces = "text/html; charset=utf-8")
    public String login(HttpServletRequest request,Model model) {
	SysMsg s = sysmsgMapper.findMsg();
	model.addAttribute("oldpath", s.getOLD_PATH());
	model.addAttribute("newpath", s.getNEW_PATH());
	return ForwardConstants.LOGIN;
    }

}
