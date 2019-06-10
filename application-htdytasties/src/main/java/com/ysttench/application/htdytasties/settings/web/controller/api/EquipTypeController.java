package com.ysttench.application.htdytasties.settings.web.controller.api;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.exception.SystemException;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.htdytasties.settings.kernel.entity.EquipTypeFormMap;
import com.ysttench.application.htdytasties.settings.kernel.mapper.EquipTypeMapper;
import com.ysttench.application.htdytasties.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htdytasties.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htdytasties.settings.web.controller.index.BaseController;

/**
 * 设备类型管理
 * 
 * @author Howard
 *
 */
@Controller
@RequestMapping("/equiptype/")
public class EquipTypeController extends BaseController {
    
    @Inject
    EquipTypeMapper equipTypeMapper;
    /**
     * 跳转到分页显示页面
     * 
     * @return
     */
    @RequestMapping("list")
    public String listUI() {
	return ForwardConstants.SYSTEM + ForwardConstants.EQUIPTYPE + ForwardConstants.LIST;

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
    public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
	EquipTypeFormMap equipTypeFormMap  = getFormMap(EquipTypeFormMap.class);
	equipTypeFormMap = toFormMap(equipTypeFormMap, pageNow, pageSize, equipTypeFormMap.getStr("orderby"));
	equipTypeFormMap.put("column", StringUtil.prop2tablefield(column));
	equipTypeFormMap.put("sort", sort);
	equipTypeFormMap.put("deleteStatus", "0");
	if (!StringUtil.isEmpty(equipTypeFormMap.getStr("equipmentType"))) {
	    equipTypeFormMap.put("equipmentType", "%"+equipTypeFormMap.getStr("equipmentType")+"%");
	}else {
	    equipTypeFormMap.remove("equipmentType");
	}
	pageView.setRecords(equipTypeMapper.findByPage(equipTypeFormMap));
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
	return ForwardConstants.SYSTEM + ForwardConstants.EQUIPTYPE + ForwardConstants.ADD;
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
	    EquipTypeFormMap equipTypeFormMap = getFormMap(EquipTypeFormMap.class);
	    equipTypeMapper.addEntity(equipTypeFormMap);
	} catch (Exception e) {
	    throw new SystemException("添加异常");
	}
	return AttrConstants.SUCCESS;
    }
    /**
     * 进入设备类型编辑画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
	String id = SessionUtil.getPara("id");
	if (StringUtil.isNotEmpty(id)) {
	    model.addAttribute("type",equipTypeMapper.findbyFrist("id", id, EquipTypeFormMap.class));
	}
	return ForwardConstants.SYSTEM + ForwardConstants.EQUIPTYPE + ForwardConstants.EDIT;
    }

    /**
     * 编辑区域保存
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    @SystemLog(module = "基础数据设置", methods = "设备管理-修改设备类型")
    /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    public String editEntity() throws Exception {
	EquipTypeFormMap equipTypeFormMap = getFormMap(EquipTypeFormMap.class);
	equipTypeMapper.editEntity(equipTypeFormMap);
	return AttrConstants.SUCCESS;

    }
    /**
     * 删除设备类型
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
    /** 需要事务操作必须加入此注解 */
    @Transactional(readOnly = false)
    /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    @SystemLog(module = "基础数据设置", methods = "设备管理-删除设备类型")
    public String deleteEntity() throws Exception {
	String[] ids = SessionUtil.getParaValues("ids");
	String[] ids1 = ids[0].split(",");
	for (String id : ids1) {
	    EquipTypeFormMap equipTypeFormMap = new EquipTypeFormMap();
	    equipTypeFormMap.put("state", "0");
	    equipTypeFormMap.put("deleteStatus", "1");
	    equipTypeFormMap.put("id", id);
	    equipTypeMapper.editEntity(equipTypeFormMap);
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 验证唯一
     * 
     * @param name
     * @return
     */
    @RequestMapping("isExist")
    @ResponseBody
    public boolean isExist(String id, String equipmentType) {
	EquipTypeFormMap equipTypeFormMap = new EquipTypeFormMap();
	equipTypeFormMap.put("id", id);
	equipTypeFormMap.put("equipmentType", equipmentType);
	List<EquipTypeFormMap> region = equipTypeMapper.count(equipTypeFormMap);
	int count = Integer.parseInt(region.get(0).get("count").toString());
	if (count > 0) {
	    return false;
	} else {
	    return true;
	}
    }
}
