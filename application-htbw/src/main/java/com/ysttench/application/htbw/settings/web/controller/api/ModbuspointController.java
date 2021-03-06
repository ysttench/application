package com.ysttench.application.htbw.settings.web.controller.api;

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
import com.ysttench.application.htbw.settings.kernel.entity.ModbusPointFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.ModbusPointMapper;
import com.ysttench.application.htbw.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htbw.settings.web.controller.index.BaseController;

@Controller
@RequestMapping("/modbuspoint/")
public class ModbuspointController extends BaseController {
    @Inject
    ModbusPointMapper modbusPointMapper;
    /**
     * 跳转列表页面
     * @return
     */
    @RequestMapping("list")
    public String listUI() {
	return ForwardConstants.API + ForwardConstants.MODBUSPOINT + ForwardConstants.LIST;
    }
    /**
     * 分页显示
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
	ModbusPointFormMap formMap = getFormMap(ModbusPointFormMap.class);
	formMap = toFormMap(formMap, pageNow, pageSize, formMap.getStr("orderby"));
	formMap.put("column", StringUtil.prop2tablefield(column));
	formMap.put("sort", sort);
	pageView.setRecords(modbusPointMapper.findMpByPage(formMap));
	return pageView;
    }
    /**
     * 跳转新增页面
     * @return
     * @throws Exception
     */
@RequestMapping("addUI")
public String addUI() throws Exception {
	return ForwardConstants.API + ForwardConstants.MODBUSPOINT + ForwardConstants.ADD;
   }
/**
 * 点位新增
 * @return
 */
@ResponseBody
@RequestMapping("addEntity")
//凡需要处理业务逻辑的.都需要记录操作日志
@SystemLog(module = "设备管理", methods = "点位管理-新增点位")
//需要事务操作必须加入此注解
@Transactional(readOnly = false)
public String addEntity() {
    ModbusPointFormMap formMap = getFormMap(ModbusPointFormMap.class);
	try {
	    formMap.put("pointName", formMap.getStr("pointName")+"("+formMap.getStr("pointVal")+")");
	    modbusPointMapper.addEntity(formMap);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SystemException("添加异常");
	}
	return AttrConstants.SUCCESS;
}
/**
 * 进入编辑页面
 * @param model
 * @return
 * @throws Exception
 */
@RequestMapping("editUI")
public String editUI(Model model) throws Exception {
	String id = SessionUtil.getPara("id");
	if (StringUtil.isNotEmpty(id)) {
	    ModbusPointFormMap formMap  = new ModbusPointFormMap();
	     formMap.put("id", id);
	    model.addAttribute("obj", modbusPointMapper.findDetail(formMap).get(0));
	}
	return ForwardConstants.API + ForwardConstants.MODBUSPOINT + ForwardConstants.EDIT;
}
/**
 * 查询led屏点位信息
 * @param upId
 * @return
 */
@ResponseBody
@RequestMapping("getled")
public List<ModbusPointFormMap> getled(String upId) {
    ModbusPointFormMap formMap = new ModbusPointFormMap();
    if (StringUtil.isEmpty(upId)) {
	formMap.put("where", "where POINT_TYPE='2'");
    }else {
	formMap.put("where", "where POINT_TYPE='2' AND ID !="+upId);
    }
    return modbusPointMapper.findByWhere(formMap);
    
}
/**
 * 删除点位
 * @return
 */
    @ResponseBody
    @RequestMapping("deleteEntity")
// 需要事务操作必须加入此注解 
    @Transactional(readOnly = false)
//凡需要处理业务逻辑的.都需要记录操作日志 
    @SystemLog(module = "设备管理", methods = "点位管理-删除点位")
    public String deleteEntity() {
	try {
	    String[] ids = SessionUtil.getParaValues("ids");
	    String[] ids1 = ids[0].split(",");
	    for (String id : ids1) {
		modbusPointMapper.deleteByAttribute("id", id, ModbusPointFormMap.class);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SystemException("删除异常");
	}
	return AttrConstants.SUCCESS;
    }
    /**
     * 编辑保存
     * @param txtGroupsSelect
     * @return
     * @throws Exception
     */
        @ResponseBody
        @RequestMapping("editEntity")
    //凡需要处理业务逻辑的.都需要记录操作日志
        @SystemLog(module = "设备管理", methods = "设备管理-修改预警设定")
        public String editEntity(String txtGroupsSelect) throws Exception {
    	ModbusPointFormMap formMap = getFormMap(ModbusPointFormMap.class);
    	try {
    	    formMap.put("pointName", formMap.getStr("pointName").substring(0, formMap.getStr("pointName").indexOf("("))+"("+formMap.getStr("pointVal")+")");
    	    modbusPointMapper.editEntity(formMap);
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    throw new SystemException("编辑异常");
    	}
    	return AttrConstants.SUCCESS;
        }
}
