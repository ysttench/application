package com.ysttench.application.htbw.settings.web.controller.api;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.htbw.settings.kernel.entity.WitchFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.WitchMapper;
import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htbw.settings.web.controller.index.BaseController;

@Controller
@RequestMapping("/witch/")
public class WitchController extends BaseController {
    @Inject
    WitchMapper witchMapper;

    /**
     * 跳转列表页面
     * 
     * @return
     */
    @RequestMapping("list")
    public String listUI() {
	return ForwardConstants.API + ForwardConstants.WITCH + ForwardConstants.LIST;
    }

    /**
     * 分页显示
     * 
     * @param pageNow
     * @param pageSize
     * @param column
     * @param sort
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findByPage")
    public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
	WitchFormMap formMap = getFormMap(WitchFormMap.class);
	formMap = toFormMap(formMap, pageNow, pageSize, formMap.getStr("orderby"));
	formMap.put("column", StringUtil.prop2tablefield(column));
	formMap.put("sort", sort);
	pageView.setRecords(witchMapper.findWitchByPage(formMap));
	return pageView;
    }
}
