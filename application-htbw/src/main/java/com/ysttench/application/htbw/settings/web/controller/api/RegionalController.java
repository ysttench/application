package com.ysttench.application.htbw.settings.web.controller.api;

import java.util.ArrayList;
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
import com.ysttench.application.htbw.settings.kernel.entity.RegionalFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.RegionalMapper;
import com.ysttench.application.htbw.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htbw.settings.web.controller.index.BaseController;

/**
 * 区域管理
 * 
 * @author Howard
 *
 */
@Controller
@RequestMapping("/regionalinfo/")
public class RegionalController extends BaseController {
    @Inject
    RegionalMapper regionalMapper;

    /**
     * 跳转到分页显示页面
     * 
     * @return
     */
    @RequestMapping("list")
    public String listUI() {
	return ForwardConstants.API + ForwardConstants.REGIONAL + ForwardConstants.LIST;

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
	RegionalFormMap regionalFormMap = getFormMap(RegionalFormMap.class);
	regionalFormMap = toFormMap(regionalFormMap, pageNow, pageSize, regionalFormMap.getStr("orderby"));
	regionalFormMap.put("column", StringUtil.prop2tablefield(column));
	regionalFormMap.put("sort", sort);
	// 不调用默认分页,调用自已的mapper中findUserPage
	pageView.setRecords(regionalMapper.findRegionalPage(regionalFormMap));
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
	return ForwardConstants.API + ForwardConstants.REGIONAL + ForwardConstants.ADD;
    }

    /**
     * 区域新增
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("addEntity")
    @SystemLog(module = "基础数据设置", methods = "区域管理-新增区域") /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    @Transactional(readOnly = false) /** 需要事务操作必须加入此注解 */
    public String addEntity() {
	try {
	    RegionalFormMap regionalFormMap = getFormMap(RegionalFormMap.class);
	    regionalMapper.addEntity(regionalFormMap);
	} catch (Exception e) {
	    throw new SystemException("添加异常");
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 删除区域
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
    /** 需要事务操作必须加入此注解 */
    @Transactional(readOnly = false)
    /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    @SystemLog(module = "基础数据设置", methods = "区域管理-删除区域")
    public String deleteEntity() throws Exception {
	String[] ids = SessionUtil.getParaValues("ids");
	String[] ids1 = ids[0].split(",");
	for (String id : ids1) {
	    RegionalFormMap regionalFormMap = regionalMapper.findbyFrist("id", id, RegionalFormMap.class);
	    if ("0".equals(regionalFormMap.get("parentId").toString())) {
		RegionalFormMap map = new RegionalFormMap();
		map.put("state", "0");
		map.put("deleteStatus", "1");
		map.put("parentId", id);
		regionalMapper.editEntity(map);
	    }
	    regionalFormMap.put("state", "0");
	    regionalFormMap.put("deleteStatus", "1");
	    regionalMapper.editEntity(regionalFormMap);
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 启用区域
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("runEntity")
    /** 需要事务操作必须加入此注解 */
    @Transactional(readOnly = false)
    /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    @SystemLog(module = "基础数据设置", methods = "区域管理-启用区域")
    public String runEntity() throws Exception {
	String[] ids = SessionUtil.getParaValues("ids");
	String[] ids1 = ids[0].split(",");
	for (String id : ids1) {
	    RegionalFormMap regionalFormMap = new RegionalFormMap();
	    regionalFormMap.put("id", id);
	    regionalFormMap.put("state", "1");
	    regionalMapper.editEntity(regionalFormMap);
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 进入区域编辑画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
	String id = SessionUtil.getPara("id");
	if (StringUtil.isNotEmpty(id)) {
	    RegionalFormMap regionalFormMap = new RegionalFormMap();
	    regionalFormMap.put("id", id);
	    List<RegionalFormMap> list = regionalMapper.findDetailRegional(regionalFormMap);
	    model.addAttribute("regional", list.get(0));
	}
	return ForwardConstants.API + ForwardConstants.REGIONAL + ForwardConstants.EDIT;
    }

    /**
     * 编辑区域保存
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    @SystemLog(module = "基础数据设置", methods = "区域管理-修改区域")
    /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    public String editEntity() throws Exception {
	RegionalFormMap regionalFormMap = getFormMap(RegionalFormMap.class);
	if (!"0".equals(regionalFormMap.getStr("parentId"))) {
	    List<RegionalFormMap> list = regionalMapper.findByAttribute("parentId", regionalFormMap.getStr("id"), RegionalFormMap.class);
	    if (list.size() != 0 ) {
		return AttrConstants.GFAIL;  
	    }
	}
	regionalMapper.editEntity(regionalFormMap);
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
    public boolean isExist(String id, String name) {
	RegionalFormMap regionalFormMap = new RegionalFormMap();
	regionalFormMap.put("id", id);
	regionalFormMap.put("name", name);
	List<RegionalFormMap> region = regionalMapper.count(regionalFormMap);
	int count = Integer.parseInt(region.get(0).get("count").toString());
	if (count > 0) {
	    return false;
	} else {
	    return true;
	}
    }

    /**
     * 获取上级位置信息
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("parentMsg")
    public List<RegionalFormMap> findAllXT(String id) {
	RegionalFormMap regionalFormMap = new RegionalFormMap();
	if (StringUtil.isEmpty(id)) {
	    regionalFormMap.put("where", "where PARENT_ID='0' and STATE='1'");
	} else {
	    regionalFormMap.put("where", "where PARENT_ID='0' and STATE='1' and id !=" + id);
	}
	return regionalMapper.findByWhere(regionalFormMap);
    }
    /**
     * 获取下级位置信息
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("getdomain")
    public List<RegionalFormMap> findAlldomain(String updomain) {
	RegionalFormMap regionalFormMap = new RegionalFormMap();
	if (!StringUtil.isEmpty(updomain)) {
	    regionalFormMap.put("where", "where PARENT_ID='"+updomain+"' and STATE='1'");
	}
	return regionalMapper.findByWhere(regionalFormMap);
    }
    /**
     * 获取所有位置信息
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("getalldomain")
    public List<RegionalFormMap> findalldomain() {
	List<RegionalFormMap> findalldomain = new ArrayList<RegionalFormMap>();
	List<RegionalFormMap> flist = regionalMapper.findByAttribute("parentId", "0", RegionalFormMap.class);
	for (RegionalFormMap map : flist) {
	    findalldomain.add(map);
	    List<RegionalFormMap> childlist = regionalMapper.findByAttribute("parentId", map.get("id").toString(), RegionalFormMap.class);
	    for (RegionalFormMap chidmap : childlist) {
		findalldomain.add(chidmap);
	    }
	}
	return findalldomain;
    }
}
