package com.ysttench.application.msd.settings.web.controller.system;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.msd.settings.kernel.entity.SysSystemForMap;
import com.ysttench.application.msd.settings.kernel.mapper.SysSystemMapper;
import com.ysttench.application.msd.settings.web.controller.common.AttrConstants;
import com.ysttench.application.msd.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.msd.settings.web.controller.index.BaseController;

/**
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/systemn/")
public class SyssystemController extends BaseController {
	@Inject
	private SysSystemMapper sysSystemMapper;

	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		List<SysSystemForMap> list1 = sysSystemMapper.findSystem(new SysSystemForMap());
		model.addAttribute("system", list1.get(0));
		return ForwardConstants.SYSTEM + ForwardConstants.SYSNAME + ForwardConstants.LIST;
	}

	@ResponseBody
	@RequestMapping("editEntity")
	@SystemLog(module = "系统管理", methods = "系统名称管理-修改系统名称")
	// 凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity() throws Exception {
		SysSystemForMap sysSystemForMap = getFormMap(SysSystemForMap.class);
		try {
			sysSystemMapper.editsystem(sysSystemForMap);
		} catch (Exception e) {
			return AttrConstants.FAIL;
		}
		return AttrConstants.SUCCESS;

	}
}