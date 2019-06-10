package com.ysttench.application.auth.settings.web.controller.system;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.auth.settings.kernel.entity.LogOperateFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.LogOperateMapper;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.util.StringUtil;

/**
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/logoperate/")
public class LogOperateController extends BaseController {
    @Inject
    private LogOperateMapper logMapper;

    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.SYSTEM + ForwardConstants.LOG_OPERATE + ForwardConstants.LIST;
    }

    @ResponseBody
    @RequestMapping("findByPage")
    public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
        LogOperateFormMap logOperateFormMap = getFormMap(LogOperateFormMap.class);
        if ((null==column) && (null == sort)) {
			logOperateFormMap.put("column", "OPERATE_TIME");
			logOperateFormMap.put("sort", "DESC");
		}else {
			logOperateFormMap.put("column", StringUtil.prop2tablefield(column));
			logOperateFormMap.put("sort", sort);
		}
        logOperateFormMap = toFormMap(logOperateFormMap, pageNow, pageSize, logOperateFormMap.getStr("orderby"));
        pageView.setRecords(logMapper.findByPage(logOperateFormMap));
        return pageView;
    }
}