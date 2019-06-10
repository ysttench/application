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
import com.ysttench.application.htbw.settings.kernel.entity.EquiptypeFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.EquiptypeMapper;
import com.ysttench.application.htbw.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htbw.settings.web.controller.index.BaseController;

@Controller
@RequestMapping("/equiptype/")
public class EquipypeController extends BaseController {
    @Inject
    EquiptypeMapper equiptypeMapper;

    /**
     * 跳转列表页面
     * 
     * @return
     */
    @RequestMapping("list")
    public String listUI() {
	return ForwardConstants.API + ForwardConstants.EQUIPTYPE + ForwardConstants.LIST;
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
	EquiptypeFormMap formMap = getFormMap(EquiptypeFormMap.class);
	formMap = toFormMap(formMap, pageNow, pageSize, formMap.getStr("orderby"));
	if (StringUtil.isNotEmpty(formMap.getStr("checkValue"))) {
	    formMap.put("equiptypeName", "%" + formMap.getStr("checkValue") + "%");
	}
	formMap.put("column", StringUtil.prop2tablefield(column));
	formMap.put("sort", sort);
	pageView.setRecords(equiptypeMapper.findByPage(formMap));
	return pageView;
    }

    /**
     * 跳转新增页面
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("addUI")
    public String addUI() throws Exception {
	return ForwardConstants.API + ForwardConstants.EQUIPTYPE + ForwardConstants.ADD;
    }

    /**
     * 新增设备分类
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("addEntity")
//凡需要处理业务逻辑的.都需要记录操作日志
    @SystemLog(module = "设备管理", methods = "设备分类管理-新增设备分类")
//需要事务操作必须加入此注解
    @Transactional(readOnly = false)
    public String addEntity() {
	EquiptypeFormMap formMap = getFormMap(EquiptypeFormMap.class);
	try {
	    equiptypeMapper.addEntity(formMap);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SystemException("添加异常");
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 删除设备分类
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
//需要事务操作必须加入此注解 
    @Transactional(readOnly = false)
//凡需要处理业务逻辑的.都需要记录操作日志 
    @SystemLog(module = "设备管理", methods = "设备分类管理-删除设备分类")
    public String deleteEntity() {
	try {
	    String[] ids = SessionUtil.getParaValues("ids");
	    String[] ids1 = ids[0].split(",");
	    for (String id : ids1) {
		equiptypeMapper.deleteByAttribute("id", id, EquiptypeFormMap.class);
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new SystemException("删除异常");
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 进入编辑界面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
	String id = SessionUtil.getPara("id");
	if (StringUtil.isNotEmpty(id)) {
	    model.addAttribute("obj", equiptypeMapper.findbyFrist("id", id, EquiptypeFormMap.class));
	}
	return ForwardConstants.API + ForwardConstants.EQUIPTYPE + ForwardConstants.EDIT;
    }

    /**
     * 编辑保存
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
//凡需要处理业务逻辑的.都需要记录操作日志
    @SystemLog(module = "设备管理", methods = "设备分类管理-修改设备分类")
    public String editEntity() throws Exception {
	EquiptypeFormMap formMap = getFormMap(EquiptypeFormMap.class);
	try {
	    equiptypeMapper.editEntity(formMap);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SystemException("编辑异常");
	}
	return AttrConstants.SUCCESS;
    }
    /**
     * 获取设备分类信息
     * @return
     */
    @ResponseBody
    @RequestMapping("getMsg")
    public List<EquiptypeFormMap> getMsg(){
	return equiptypeMapper.findByWhere(new EquiptypeFormMap());
    }
}
