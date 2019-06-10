package com.ysttench.application.htbw.settings.web.controller.api;

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
import com.ysttench.application.htbw.settings.kernel.entity.LogRecordFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.LogRecordMapper;
import com.ysttench.application.htbw.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htbw.settings.web.controller.index.BaseController;

@Controller
@RequestMapping("/logrecord/")
public class LogRecordController extends BaseController {
    @Inject
    LogRecordMapper logRecordMapper;

    /**
     * 跳转列表界面
     * 
     * @return
     */
    @RequestMapping("list")
    public String listUI() {
	return ForwardConstants.API + ForwardConstants.LOGRECORD + ForwardConstants.LIST;
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
	LogRecordFormMap formMap = getFormMap(LogRecordFormMap.class);
	formMap = toFormMap(formMap, pageNow, pageSize, formMap.getStr("orderby"));
	if (StringUtil.isNotEmpty(formMap.getStr("checkValue"))) {
	    formMap.put("jbName", "%" + formMap.getStr("checkValue") + "%");
	}
	formMap.put("column", StringUtil.prop2tablefield(column));
	formMap.put("sort", sort);
	pageView.setRecords(logRecordMapper.findByPage(formMap));
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
	return ForwardConstants.API + ForwardConstants.LOGRECORD + ForwardConstants.ADD;
    }

    /**
     * 新增交班记录
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("addEntity")
//凡需要处理业务逻辑的.都需要记录操作日志
    @SystemLog(module = "考勤管理", methods = "考勤管理-新增交班记录")
// 需要事务操作必须加入此注解
    @Transactional(readOnly = false)
    public String addEntity() {
	LogRecordFormMap formMap = getFormMap(LogRecordFormMap.class);
	try {
	    formMap.put("jbName", SessionUtil.getSessionAttr("userName"));
	    String jbDesc = new String(formMap.getStr("jbDesc").getBytes("iso-8859-1"), "utf-8");
	    formMap.put("jbDesc", jbDesc);
	    logRecordMapper.addEntity(formMap);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SystemException("添加异常");
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 删除交班记录
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
// 需要事务操作必须加入此注解 
    @Transactional(readOnly = false)
//凡需要处理业务逻辑的.都需要记录操作日志 
    @SystemLog(module = "考勤管理", methods = "考勤管理-删除交班记录")
    public String deleteEntity() {
	try {
	    String[] ids = SessionUtil.getParaValues("ids");
	    String[] ids1 = ids[0].split(",");
	    for (String id : ids1) {
		logRecordMapper.deleteByAttribute("id", id, LogRecordFormMap.class);
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new SystemException("删除异常");
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 跳转编辑界面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
	String id = SessionUtil.getPara("id");
	if (StringUtil.isNotEmpty(id)) {
	    model.addAttribute("obj", logRecordMapper.findbyFrist("id", id, LogRecordFormMap.class));
	}
	return ForwardConstants.API + ForwardConstants.LOGRECORD + ForwardConstants.EDIT;
    }

    /**
     * 编辑保存
     * 
     * @param txtGroupsSelect
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
//凡需要处理业务逻辑的.都需要记录操作日志
    @SystemLog(module = "考勤管理", methods = "设考勤管理-修改交班记录")
    public String editEntity(String txtGroupsSelect) throws Exception {
	LogRecordFormMap formMap = getFormMap(LogRecordFormMap.class);
	try {
	    logRecordMapper.editEntity(formMap);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SystemException("编辑异常");
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 获取交班记录数量
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("jbjlNum")
    public int jbjlNum() {
	return logRecordMapper.findByWhere(new LogRecordFormMap()).size();

    }
}
