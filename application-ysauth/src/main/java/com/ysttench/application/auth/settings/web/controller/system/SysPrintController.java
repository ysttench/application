package com.ysttench.application.auth.settings.web.controller.system;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.auth.settings.kernel.entity.SysPrintForMap;
import com.ysttench.application.auth.settings.kernel.mapper.SysPrintMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.StringUtil;

/**
 * 打印机配置
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/print/")
public class SysPrintController extends BaseController {
	@Inject
	private SysPrintMapper sysPrintMapper;

	/**
	 * 列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	public String listUI() throws Exception {
		return ForwardConstants.SYSTEM + ForwardConstants.PRINT + ForwardConstants.LIST;
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
		SysPrintForMap sysPrintForMap = getFormMap(SysPrintForMap.class);
		sysPrintForMap = toFormMap(sysPrintForMap, pageNow, pageSize, sysPrintForMap.getStr("orderby"));
		sysPrintForMap.put("column", StringUtil.prop2tablefield(column));
		sysPrintForMap.put("sort", sort);
		if (pageNow == null) {
			pageNow = "1";
		}
		sysPrintForMap.put("pageNow", pageNow);
		sysPrintForMap.put("pageSize", pageSize);
		pageView.setRecords(sysPrintMapper.findPrintPage(sysPrintForMap));
		return pageView;
	}

	/**
	 * 跳转新增
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return ForwardConstants.SYSTEM + ForwardConstants.PRINT + ForwardConstants.ADD;
	}

	/**
	 * 新增实现
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("addEntity")
	@Transactional(readOnly = false)
	// 需要事务操作必须加入此注解
	@SystemLog(module = "系统管理", methods = "打印机管理-新增打印机")
	// 凡需要处理业务逻辑的.都需要记录操作日志
	public String addEntity() throws Exception {
		SysPrintForMap sysPrintForMap = getFormMap(SysPrintForMap.class);
		sysPrintMapper.addEntity(sysPrintForMap);
		return AttrConstants.SUCCESS;
	}

	/**
	 * 删除
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("deleteEntity")
	@Transactional(readOnly = false)
	// 需要事务操作必须加入此注解
	@SystemLog(module = "系统管理", methods = "打印机管理-删除打印机")
	// 凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		String[] ids = SessionUtil.getParaValues("ids");
		String[] ids1 = ids[0].split(",");
		for (String id : ids1) {
			SysPrintForMap sysPrintForMap = new SysPrintForMap();
			sysPrintForMap.put("id", id);
			sysPrintMapper.deleteByAttribute(sysPrintForMap);
		}
		return AttrConstants.SUCCESS;
	}

	/**
	 * 跳转编辑
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		if (StringUtil.isNotEmpty(id)) {
			SysPrintForMap sysPrintForMap = new SysPrintForMap();
			sysPrintForMap.put("id", id);
			model.addAttribute("dprint", sysPrintMapper.findbyFrist(sysPrintForMap));
		}
		return ForwardConstants.SYSTEM + ForwardConstants.PRINT + ForwardConstants.EDIT;
	}

	/**
	 * 编辑实现
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly = false)
	// 需要事务操作必须加入此注解
	@SystemLog(module = "系统管理", methods = "打印机管理-修改打印机")
	// 凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity() throws Exception {
		SysPrintForMap sysPrintForMap = getFormMap(SysPrintForMap.class);
		sysPrintMapper.editEntity(sysPrintForMap);
		return AttrConstants.SUCCESS;
	}

	/**
	 * 验证打印机是否存在
	 * 
	 * @param roleName
	 * @return
	 */
	@RequestMapping("isExist")
	@ResponseBody
	public boolean isExist(String id, String wsdlUrl, String organ, String type) {
		SysPrintForMap sysPrintForMap = new SysPrintForMap();
		sysPrintForMap.put("id", id);
		sysPrintForMap.put("wsdlUrl", wsdlUrl);
		sysPrintForMap.put("organ", organ);
		sysPrintForMap.put("type", type);
		List<SysPrintForMap> role = sysPrintMapper.count(sysPrintForMap);
		int count = Integer.parseInt(role.get(0).get("count").toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取打印机信息
	 * 
	 * @return
	 */
	@RequestMapping("getMsg")
	@ResponseBody
	public List<SysPrintForMap> getMsg() {
		SysPrintForMap sysPrintForMap = new SysPrintForMap();
		return sysPrintMapper.findPrintPage(sysPrintForMap);

	}

}