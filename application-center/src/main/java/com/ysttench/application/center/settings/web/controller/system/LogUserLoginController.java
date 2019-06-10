package com.ysttench.application.center.settings.web.controller.system;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.center.settings.kernel.entity.LogUserLoginFormMap;
import com.ysttench.application.center.settings.kernel.mapper.LogUserLoginMapper;
import com.ysttench.application.center.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.center.settings.web.controller.index.BaseController;

/**
 * 
 * @author howard
 */
@Controller
@RequestMapping("/loguserlogin/")
public class LogUserLoginController extends BaseController {
    @Inject
    private LogUserLoginMapper userLoginMapper;

    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.SYSTEM + ForwardConstants.LOG_USER_LOGIN + ForwardConstants.LIST;
    }

    @ResponseBody
    @RequestMapping("findByPage")
    public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
        LogUserLoginFormMap logUserLoginFormMap = getFormMap(LogUserLoginFormMap.class);
        if ((null==column) && (null == sort)) {
        	logUserLoginFormMap.put("column", "LOGIN_TIME");
        	logUserLoginFormMap.put("sort", "DESC");
        }else {
        	logUserLoginFormMap.put("column", StringUtil.prop2tablefield(column));
        	logUserLoginFormMap.put("sort", sort);
		}
        logUserLoginFormMap = toFormMap(logUserLoginFormMap, pageNow, pageSize, logUserLoginFormMap.getStr("orderby"));
        pageView.setRecords(userLoginMapper.findByPage(logUserLoginFormMap));
        return pageView;
    }

}