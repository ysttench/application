package com.ysttench.application.equip.settings.web.controller.system;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.equip.settings.kernel.entity.LogOperateFormMap;
import com.ysttench.application.equip.settings.kernel.mapper.LogOperateMapper;
import com.ysttench.application.equip.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.equip.settings.web.controller.index.BaseController;

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
		logOperateFormMap.put("column", StringUtil.prop2tablefield(column));
		logOperateFormMap.put("sort", sort);
		logOperateFormMap = toFormMap(logOperateFormMap, pageNow, pageSize, logOperateFormMap.getStr("orderby"));
		pageView.setRecords(logMapper.findByPage(logOperateFormMap));
		return pageView;
	}
}