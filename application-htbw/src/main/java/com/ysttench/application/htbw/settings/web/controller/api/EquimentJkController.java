package com.ysttench.application.htbw.settings.web.controller.api;

import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.htbw.settings.kernel.entity.EquipmentMsgFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.EquipmentMapper;
import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htbw.settings.web.controller.index.BaseController;

@Controller
@RequestMapping("/equipmentjk/")
public class EquimentJkController extends BaseController{
    @Inject
    EquipmentMapper equipmentMapper;
    /**
     * 跳转列表页面
     * 
     * @return
     */
@RequestMapping("list")
public String listUI() {
	return ForwardConstants.API + ForwardConstants.EQUIPMENTJK + ForwardConstants.LIST;
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
	EquipmentMsgFormMap formMap = getFormMap(EquipmentMsgFormMap.class);
	formMap = toFormMap(formMap, pageNow, pageSize, formMap.getStr("orderby"));
	if (StringUtil.isEmpty(column)) {
	    formMap.put("column", "EQUIPMENT_NUM");
	    formMap.put("sort", "desc"); 
	}else {
	    formMap.put("column", StringUtil.prop2tablefield(column));
	    formMap.put("sort", sort);
	}
	pageView.setRecords(equipmentMapper.findJkMsg(formMap));
	return pageView;
    }
}
