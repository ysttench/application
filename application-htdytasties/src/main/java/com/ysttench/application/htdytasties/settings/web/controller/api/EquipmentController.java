package com.ysttench.application.htdytasties.settings.web.controller.api;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.exception.SystemException;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.htdytasties.settings.kernel.entity.EquipmentFormMap;
import com.ysttench.application.htdytasties.settings.kernel.mapper.EquipmentMapper;
import com.ysttench.application.htdytasties.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htdytasties.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htdytasties.settings.web.controller.index.BaseController;

/**
 * 设备管理
 * 
 * @author Howard
 *
 */
@Controller
@RequestMapping("/equipment/")
public class EquipmentController extends BaseController {
    @Inject
    EquipmentMapper equipmentMapper;
    
    /**
     * 跳转到分页显示页面
     * 
     * @return
     */
    @RequestMapping("list")
    public String listUI() {
	return ForwardConstants.API + ForwardConstants.EQUIPMENT + ForwardConstants.LIST;

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
	EquipmentFormMap equipmentFormMap  = getFormMap(EquipmentFormMap.class);
	equipmentFormMap = toFormMap(equipmentFormMap, pageNow, pageSize, equipmentFormMap.getStr("orderby"));
	equipmentFormMap.put("column", StringUtil.prop2tablefield(column));
	equipmentFormMap.put("sort", sort);
	equipmentFormMap.put("deleteStatus", "0");
	pageView.setRecords(equipmentMapper.findEquipPage(equipmentFormMap));
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
	return ForwardConstants.API + ForwardConstants.EQUIPMENT + ForwardConstants.ADD;
    }

    /**
     * 设备类型新增
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("addEntity")
    @SystemLog(module = "基础数据设置", methods = "设备管理-新增设备类型") /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    @Transactional(readOnly = false) /** 需要事务操作必须加入此注解 */
    public String addEntity() {
	try {
	    EquipmentFormMap equipmentFormMap = getFormMap(EquipmentFormMap.class);
	    equipmentMapper.addEntity(equipmentFormMap);
	} catch (Exception e) {
	    throw new SystemException("添加异常");
	}
	return AttrConstants.SUCCESS;
    }
}
