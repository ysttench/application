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
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.htbw.settings.kernel.entity.AnnountFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.AnnountMapper;
import com.ysttench.application.htbw.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htbw.settings.web.controller.index.BaseController;

@Controller
@RequestMapping("/annount/")
public class AnnountController extends BaseController {
    @Inject
    AnnountMapper annountMapper;

    /**
     * 跳转列表页面
     * 
     * @return
     */
    @RequestMapping("list")
    public String listUI() {
	return ForwardConstants.API + ForwardConstants.ANNOUNT + ForwardConstants.LIST;
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
	AnnountFormMap formMap = getFormMap(AnnountFormMap.class);
	formMap = toFormMap(formMap, pageNow, pageSize, formMap.getStr("orderby"));
	if (StringUtil.isNotEmpty(formMap.getStr("checkValue"))) {
	    formMap.put("ggDesc", "%" + formMap.getStr("checkValue") + "%");
	    formMap.put("ggMsg", "%" + formMap.getStr("checkValue") + "%");
	}
	formMap.put("column", StringUtil.prop2tablefield(column));
	formMap.put("sort", sort);
	pageView.setRecords(annountMapper.findByPage(formMap));
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
	return ForwardConstants.API + ForwardConstants.ANNOUNT + ForwardConstants.ADD;
    }

    /**
     * 新增通知公告
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("addEntity")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    @SystemLog(module = "考勤管理", methods = "通知管理-新增通知公告")
    // 需要事务操作必须加入此注解
    @Transactional(readOnly = false)
    public String addEntity() {
	AnnountFormMap formMap = getFormMap(AnnountFormMap.class);
	try {
	    formMap.put("fbName", SessionUtil.getSessionAttr("userName"));
	    formMap.put("fbDate", DatetimeUtil.fromDateH());
	    annountMapper.addEntity(formMap);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SystemException("添加异常");
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 删除通知公告
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
    // 需要事务操作必须加入此注解
    @Transactional(readOnly = false)
    // 凡需要处理业务逻辑的.都需要记录操作日志
    @SystemLog(module = "考勤管理", methods = "通知管理-删除通知公告")
    public String deleteEntity() {
	try {
	    String[] ids = SessionUtil.getParaValues("ids");
	    String[] ids1 = ids[0].split(",");
	    for (String id : ids1) {
		annountMapper.deleteByAttribute("id", id, AnnountFormMap.class);
	    }
	} catch (Exception e) {
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
	    model.addAttribute("obj", annountMapper.findbyFrist("id", id, AnnountFormMap.class));
	}
	return ForwardConstants.API + ForwardConstants.ANNOUNT + ForwardConstants.EDIT;
    }

    /**
     * 编辑保存
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    @SystemLog(module = "考勤管理", methods = "通知管理-修改通知公告")
    public String editEntity() throws Exception {
	AnnountFormMap formMap = getFormMap(AnnountFormMap.class);
	try {
	    annountMapper.editEntity(formMap);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SystemException("编辑异常");
	}
	return AttrConstants.SUCCESS;
    }
    /**
     * 获取公告数量
     * @return
     */
    @ResponseBody
    @RequestMapping("glNum")
    public int glNum() {
	return annountMapper.findByWhere(new AnnountFormMap()).size();
	
    }
}
