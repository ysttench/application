package com.ysttench.application.equip.settings.web.controller.api;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.equip.settings.kernel.entity.ApiUnitFormMap;
import com.ysttench.application.equip.settings.kernel.mapper.ApiUnitMapper;
import com.ysttench.application.equip.settings.web.controller.common.AttrConstants;
import com.ysttench.application.equip.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.equip.settings.web.controller.index.BaseController;
@Controller
@RequestMapping("/apiUnit/")
public class ApiUnitController extends BaseController {
@Inject
ApiUnitMapper apiUnitMapper;
/**
 * 进入数量单位管理界面
 * @param model
 * @return
 * @throws Exception
 */
@RequestMapping("list")
public String listUI(Model model) throws Exception{
	return ForwardConstants.API+ForwardConstants.UNIT+ForwardConstants.LIST;
	
}
/**
 * 数量单位页面分页处理
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
	ApiUnitFormMap apiUnitFormMap = getFormMap(ApiUnitFormMap.class);
	apiUnitFormMap = toFormMap(apiUnitFormMap, pageNow, pageSize, apiUnitFormMap.getStr("orderby"));
	apiUnitFormMap.put("column", StringUtil.prop2tablefield(column));
	apiUnitFormMap.put("sort", sort);
	pageView.setRecords(apiUnitMapper.findUnitByPage(apiUnitFormMap));
	return pageView;
	
}
/**
 * 新增数量单位
 * @param model
 * @return
 * @throws Exception
 */
@RequestMapping("addUI")
public String addUI(Model model) throws Exception {
    return ForwardConstants.API+ForwardConstants.UNIT+ForwardConstants.ADD;
}
/**
 * 添加数量单位
 * 
 * @return
 */
@ResponseBody
@RequestMapping("addEntity")
@SystemLog(module = "应用管理", methods = "数量单位管理-新增数量单位") // 凡需要处理业务逻辑的.都需要记录操作日志
@Transactional(readOnly = false) // 需要事务操作必须加入此注解
public String addEntity() {
	try {
		ApiUnitFormMap apiUnitFormMap = getFormMap(ApiUnitFormMap.class);
		apiUnitMapper.insertUnit(apiUnitFormMap);
	} catch (Exception e) {
		e.printStackTrace();
		logger.error("添加数量单位异常");
		return AttrConstants.FAIL;
	}
	return AttrConstants.SUCCESS;
}
/**
 * 进入数量单位编辑页面
 * @param model
 * @return
 * @throws Exception
 */
@RequestMapping("editUI")
public String editUI(Model model)throws Exception  {
	String id = SessionUtil.getPara("id");
	if (StringUtil.isNotEmpty(id)) {
		ApiUnitFormMap apiUnitFormMap = new ApiUnitFormMap();
		apiUnitFormMap.put("id", id);
		ApiUnitFormMap  apiunit= apiUnitMapper.findDetailUnit(apiUnitFormMap).get(0);
        model.addAttribute("apiunit", apiunit);
    }
    return ForwardConstants.API+ForwardConstants.UNIT+ForwardConstants.EDIT;
}
/**
 * 编辑设备保存
 * @return
 * @throws Exception
 */
@ResponseBody
@RequestMapping("editEntity")
@Transactional(readOnly = false)
// 需要事务操作必须加入此注解
@SystemLog(module = "应用管理", methods = "办公位置管理-修改位置")
// 凡需要处理业务逻辑的.都需要记录操作日志
public String editEntity()throws Exception{
	ApiUnitFormMap apiUnitFormMap = getFormMap(ApiUnitFormMap.class);
	try {
		apiUnitMapper.editUnit(apiUnitFormMap);
	} catch (Exception e) {
		e.printStackTrace();
        logger.error("更新数量单位异常");
        return AttrConstants.FAIL;
	}
	return AttrConstants.SUCCESS;
}
/**
 * 删除设备
 * 
 * @return
 * @throws Exception
 */
@ResponseBody
@RequestMapping("deleteEntity")
@Transactional(readOnly = false)
// 需要事务操作必须加入此注解
@SystemLog(module = "应用管理", methods = "办公位置管理-删除位置")
// 凡需要处理业务逻辑的.都需要记录操作日志
public String deleteEntity() {
    String[] ids = SessionUtil.getParaValues("ids");
    for (String id : ids) {
    	ApiUnitFormMap apiUnitFormMap = new ApiUnitFormMap();
    	apiUnitFormMap.put("id", id);
    	apiUnitMapper.deleteById(apiUnitFormMap);
      
    }
    return AttrConstants.SUCCESS;
}
/**
 * 验证唯一性
 * @param locationName
 * @param locationCode
 * @param id
 * @return
 */
@ResponseBody
@RequestMapping("isExist")
public boolean IsOnly(String name,String code,String id){
	ApiUnitFormMap apiUnitFormMap = new ApiUnitFormMap();
	apiUnitFormMap.put("name", name);
	apiUnitFormMap.put("code", code);
	apiUnitFormMap.put("id", id);
	List<ApiUnitFormMap> unit = apiUnitMapper.count(apiUnitFormMap);
    int count = Integer.parseInt(unit.get(0).get("count").toString());
    if (count > 0) {
        return false;
    } else {
        return true;
    }
}
@ResponseBody
@RequestMapping("sldw")
public List<ApiUnitFormMap> findAll(){
	return apiUnitMapper.findUnitByPage(new ApiUnitFormMap());
}
}
