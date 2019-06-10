package com.ysttench.application.auth.settings.web.controller.api;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.auth.settings.kernel.entity.ApiAssetsTypeFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.ApiAssetsTypeMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.StringUtil;

@Controller
@RequestMapping("/assetsType/")
public class ApiAssetsTypeController extends BaseController {
	@Inject
	ApiAssetsTypeMapper apiAssetsTypeMapper;

	/**
	 * 进入数量单位管理界面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.ASSETSTYPE + ForwardConstants.LIST;

	}

	/**
	 * 数量单位页面分页处理
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
		ApiAssetsTypeFormMap apiAssetsTypeFormMap = getFormMap(ApiAssetsTypeFormMap.class);
		apiAssetsTypeFormMap = toFormMap(apiAssetsTypeFormMap, pageNow, pageSize,
				apiAssetsTypeFormMap.getStr("orderby"));
		apiAssetsTypeFormMap.put("column", StringUtil.prop2tablefield(column));
		apiAssetsTypeFormMap.put("sort", sort);
		pageView.setRecords(apiAssetsTypeMapper.findAssetsTypeByPage(apiAssetsTypeFormMap));
		return pageView;

	}

	/**
	 * 新增数量单位
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.ASSETSTYPE + ForwardConstants.ADD;
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
			ApiAssetsTypeFormMap apiAssetsTypeFormMap = getFormMap(ApiAssetsTypeFormMap.class);
			apiAssetsTypeMapper.insertAssetsType(apiAssetsTypeFormMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("添加资产分类异常");
			return AttrConstants.FAIL;
		}
		return AttrConstants.SUCCESS;
	}

	/**
	 * 进入数量单位编辑页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		if (StringUtil.isNotEmpty(id)) {
			ApiAssetsTypeFormMap apiAssetsTypeFormMap = new ApiAssetsTypeFormMap();
			apiAssetsTypeFormMap.put("id", id);
			ApiAssetsTypeFormMap apitype = apiAssetsTypeMapper.findDetailType(apiAssetsTypeFormMap).get(0);
			model.addAttribute("apitype", apitype);
		}
		return ForwardConstants.API + ForwardConstants.ASSETSTYPE + ForwardConstants.EDIT;
	}

	/**
	 * 编辑设备保存
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("editEntity")
	@Transactional(readOnly = false)
	// 需要事务操作必须加入此注解
	@SystemLog(module = "应用管理", methods = "资产分类管理-修改资产分类")
	// 凡需要处理业务逻辑的.都需要记录操作日志
	public String editEntity() throws Exception {
		ApiAssetsTypeFormMap apiAssetsTypeFormMap = getFormMap(ApiAssetsTypeFormMap.class);
		try {
			apiAssetsTypeMapper.editType(apiAssetsTypeFormMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("更新资产分类异常");
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
			ApiAssetsTypeFormMap apiAssetsTypeFormMap = new ApiAssetsTypeFormMap();
			apiAssetsTypeFormMap.put("id", id);
			apiAssetsTypeMapper.deleteById(apiAssetsTypeFormMap);

		}
		return AttrConstants.SUCCESS;
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
	public boolean IsOnly(String name, String code, String id) {
		ApiAssetsTypeFormMap apiAssetsTypeFormMap = new ApiAssetsTypeFormMap();
		apiAssetsTypeFormMap.put("name", name);
		apiAssetsTypeFormMap.put("code", code);
		apiAssetsTypeFormMap.put("id", id);
		List<ApiAssetsTypeFormMap> unit = apiAssetsTypeMapper.count(apiAssetsTypeFormMap);
		int count = Integer.parseInt(unit.get(0).get("count").toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("zcfl")
	public List<ApiAssetsTypeFormMap> findAllXT() {
		return apiAssetsTypeMapper.findType(new ApiAssetsTypeFormMap());
	}
}
