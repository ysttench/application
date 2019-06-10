package com.ysttench.application.auth.settings.web.controller.api;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.auth.settings.kernel.entity.ApiOfficeFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.ApiOfficeMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.StringUtil;

@Controller
@RequestMapping("/apiOffice/")
public class ApiOfficeController extends BaseController {
	@Inject
	ApiOfficeMapper apiOfficeMapper;

	/**
	 * 进入办公位置管理界面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.OFFICE + ForwardConstants.LIST;

	}

	/**
	 * 办公位置页面分页处理
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
		ApiOfficeFormMap apiOfficeFormMap = getFormMap(ApiOfficeFormMap.class);
		apiOfficeFormMap = toFormMap(apiOfficeFormMap, pageNow, pageSize, apiOfficeFormMap.getStr("orderby"));
		apiOfficeFormMap.put("column", StringUtil.prop2tablefield(column));
		apiOfficeFormMap.put("sort", sort);
		pageView.setRecords(apiOfficeMapper.findOfficeByPage(apiOfficeFormMap));
		return pageView;

	}

	/**
	 * 新增办公位置
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.OFFICE + ForwardConstants.ADD;
	}

	/**
	 * 添加办公位置
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("addEntity")
	@SystemLog(module = "应用管理", methods = "办公位置管理-新增位置") // 凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly = false) // 需要事务操作必须加入此注解
	public String addEntity() {
		try {
			ApiOfficeFormMap apiOfficeFormMap = getFormMap(ApiOfficeFormMap.class);
			apiOfficeMapper.insertOffice(apiOfficeFormMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加办公位置异常");
			return AttrConstants.FAIL;
		}
		return AttrConstants.SUCCESS;
	}

	/**
	 * 进入办公位置编辑页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		if (StringUtil.isNotEmpty(id)) {
			ApiOfficeFormMap apiOfficeFormMap = new ApiOfficeFormMap();
			apiOfficeFormMap.put("id", id);
			ApiOfficeFormMap apioffice = apiOfficeMapper.findDetailOffice(apiOfficeFormMap).get(0);
			model.addAttribute("apiOffice", apioffice);
		}
		return ForwardConstants.API + ForwardConstants.OFFICE + ForwardConstants.EDIT;
	}

	/**
	 * 编辑仓储区域保存
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly = false)
	// 需要事务操作必须加入此注解
	@SystemLog(module = "应用管理", methods = "办公位置管理-修改位置")
	// 凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity() throws Exception {
		ApiOfficeFormMap apiOfficeFormMap = getFormMap(ApiOfficeFormMap.class);
		try {
			apiOfficeMapper.editOffice(apiOfficeFormMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新办公位置异常");
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
			ApiOfficeFormMap apiOfficeFormMap = new ApiOfficeFormMap();
			apiOfficeFormMap.put("id", id);
			apiOfficeMapper.deleteById(apiOfficeFormMap);

		}
		return AttrConstants.SUCCESS;
	}

	/**
	 * 获取上级位置信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("sjbgwz")
	public List<ApiOfficeFormMap> findAllXT(String id) {
		ApiOfficeFormMap apiOfficeFormMap = new ApiOfficeFormMap();
		apiOfficeFormMap.put("id", id);
		return apiOfficeMapper.findsuperOffice(apiOfficeFormMap);
	}

	/**
	 * 获取上级位置信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("superplace")
	public List<ApiOfficeFormMap> findAllUpPlace(String locationCode) {
		List<ApiOfficeFormMap> list = new ArrayList<ApiOfficeFormMap>();
		ApiOfficeFormMap apiOfficeFormMap = new ApiOfficeFormMap();
		if (StringUtil.isEmpty(locationCode) || "0".equals(locationCode)) {
			list = apiOfficeMapper.findsuperOffice(apiOfficeFormMap);
		} else {
			apiOfficeFormMap.put("locationCode", locationCode);
			list = apiOfficeMapper.findupOffice(apiOfficeFormMap);
		}
		return list;
	}

	/**
	 * 验证唯一性
	 * 
	 * @param locationName
	 * @param locationCode
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("isExist")
	public boolean IsOnly(String locationName, String locationCode, String id) {
		ApiOfficeFormMap apiOfficeFormMap = new ApiOfficeFormMap();
		apiOfficeFormMap.put("locationName", locationName);
		apiOfficeFormMap.put("locationCode", locationCode);
		apiOfficeFormMap.put("id", id);
		List<ApiOfficeFormMap> office = apiOfficeMapper.count(apiOfficeFormMap);
		int count = Integer.parseInt(office.get(0).get("count").toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}
}
