package com.ysttench.application.htdytasties.settings.web.controller.api;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.htdytasties.settings.kernel.entity.NoticeFormMap;
import com.ysttench.application.htdytasties.settings.kernel.mapper.NoticeMapper;
import com.ysttench.application.htdytasties.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htdytasties.settings.web.controller.index.BaseController;

/**
 * 设备管理
 * 
 * @author Howard
 *
 */
@Controller
@RequestMapping("/notice/")
public class NoticeController extends BaseController {
    @Inject
    NoticeMapper noticeMapper;
    
    /**
     * 跳转到分页显示页面
     * 
     * @return
     */
    @RequestMapping("list")
    public String listUI() {
	return ForwardConstants.API + ForwardConstants.NOTICE + ForwardConstants.LIST;

    }
    /**
     * 分页显示数据
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
    public PageView findByPage(String pageNow, String pageSize, String column, String sort,String searchValue) throws Exception {
	NoticeFormMap formMap  = getFormMap(NoticeFormMap.class);
	formMap = toFormMap(formMap, pageNow, pageSize, formMap.getStr("orderby"));
	formMap.put("column", StringUtil.prop2tablefield(column));
	formMap.put("sort", sort);
	formMap.put("deleteStatus", "0");
	pageView.setRecords(noticeMapper.findByPage(formMap));
	return pageView;
    }
    /**
     * 跳转到新增页面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("addUI")
    public String addUI(Model model) throws Exception {
	return ForwardConstants.API + ForwardConstants.NOTICE + ForwardConstants.ADD;
    }
}
