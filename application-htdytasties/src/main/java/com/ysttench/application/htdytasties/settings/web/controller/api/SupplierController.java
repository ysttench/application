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
import com.ysttench.application.htdytasties.settings.kernel.entity.SupplierFormMap;
import com.ysttench.application.htdytasties.settings.kernel.mapper.EquipTypeMapper;
import com.ysttench.application.htdytasties.settings.kernel.mapper.SupplierMapper;
import com.ysttench.application.htdytasties.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htdytasties.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htdytasties.settings.web.controller.index.BaseController;

/**
 * 供应商管理
 * 
 * @author Howard
 *
 */
@Controller
@RequestMapping("/supplier/")
public class SupplierController extends BaseController {
    
    @Inject
    EquipTypeMapper equipTypeMapper;
    @Inject
    SupplierMapper supplierMapper;
    /**
     * 跳转到分页显示页面
     * 
     * @return
     */
    @RequestMapping("list")
    public String listUI() {
	return ForwardConstants.API + ForwardConstants.SUPPLIER + ForwardConstants.LIST;

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
	SupplierFormMap supplierFormMap  = getFormMap(SupplierFormMap.class);
	supplierFormMap = toFormMap(supplierFormMap, pageNow, pageSize, supplierFormMap.getStr("orderby"));
	supplierFormMap.put("column", StringUtil.prop2tablefield(column));
	supplierFormMap.put("sort", sort);
	supplierFormMap.put("deleteStatus", "0");
	if (!StringUtil.isEmpty(supplierFormMap.getStr("name"))) {
	    supplierFormMap.put("name", "%"+supplierFormMap.getStr("name")+"%");
	}else {
	    supplierFormMap.remove("name");
	}
	pageView.setRecords(supplierMapper.findByPage(supplierFormMap));
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
	return ForwardConstants.API + ForwardConstants.SUPPLIER + ForwardConstants.ADD;
    }

    /**
     * 设备类型新增
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("addEntity")
    @SystemLog(module = "基础数据设置", methods = "基础管理-新增供应商") /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    @Transactional(readOnly = false) /** 需要事务操作必须加入此注解 */
    public String addEntity() {
	try {
	    SupplierFormMap supplierFormMap = getFormMap(SupplierFormMap.class);
	    supplierMapper.addEntity(supplierFormMap);
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
	    model.addAttribute("supplier",supplierMapper.findbyFrist("id", id, SupplierFormMap.class));
	}
	return ForwardConstants.API + ForwardConstants.SUPPLIER + ForwardConstants.EDIT;
    }

    /**
     * 编辑区域保存
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    @SystemLog(module = "基础数据设置", methods = "基础管理-修改供应商")
    /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    public String editEntity() throws Exception {
	SupplierFormMap supplierFormMap = getFormMap(SupplierFormMap.class);
	supplierMapper.editEntity(supplierFormMap);
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
    @SystemLog(module = "基础数据设置", methods = "基础管理-删除供应商")
    public String deleteEntity() throws Exception {
	String[] ids = SessionUtil.getParaValues("ids");
	String[] ids1 = ids[0].split(",");
	for (String id : ids1) {
	    SupplierFormMap supplierFormMap = new SupplierFormMap();
	    supplierFormMap.put("state", "0");
	    supplierFormMap.put("deleteStatus", "1");
	    supplierFormMap.put("id", id);
	    supplierMapper.editEntity(supplierFormMap);
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
    public boolean isExist(String id, String name,String phone) {
	SupplierFormMap supplierFormMap = new SupplierFormMap();
	supplierFormMap.put("id", id);
	supplierFormMap.put("name", name);
	supplierFormMap.put("phone", phone);
	List<SupplierFormMap> region = supplierMapper.count(supplierFormMap);
	int count = Integer.parseInt(region.get(0).get("count").toString());
	if (count > 0) {
	    return false;
	} else {
	    return true;
	}
    }
}
