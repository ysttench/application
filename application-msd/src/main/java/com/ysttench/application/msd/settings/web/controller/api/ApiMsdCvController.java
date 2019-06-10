package com.ysttench.application.msd.settings.web.controller.api;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.msd.settings.kernel.entity.ApiMsdCVFormMap;
import com.ysttench.application.msd.settings.kernel.mapper.ApiMsdCVMapper;
import com.ysttench.application.msd.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.msd.settings.web.controller.index.BaseController;

/**
 * msd履历
 * @author Howard
 *
 */
@Controller
@RequestMapping("/msdcv/")
public class ApiMsdCvController extends BaseController{
	@Inject
	ApiMsdCVMapper apiMsdCVMapper;
	@RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.API + ForwardConstants.MSDCV + ForwardConstants.LIST;
    }
	@ResponseBody
    @RequestMapping("findByPage")
	public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
		ApiMsdCVFormMap apiMsdCVFormMap = getFormMap(ApiMsdCVFormMap.class);
		apiMsdCVFormMap = toFormMap(apiMsdCVFormMap, pageNow, pageSize, apiMsdCVFormMap.getStr("orderby"));
		apiMsdCVFormMap.put("column", StringUtil.prop2tablefield(column));
		apiMsdCVFormMap.put("sort", sort);
		pageView.setRecords(apiMsdCVMapper.findMSDCVPage(apiMsdCVFormMap));// 不调用默认分页,调用自已的mapper中findUserPage
        return pageView;
	}
}
