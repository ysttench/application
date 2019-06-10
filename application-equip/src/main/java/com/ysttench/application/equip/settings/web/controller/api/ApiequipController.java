package com.ysttench.application.equip.settings.web.controller.api;

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
import com.ysttench.application.equip.settings.kernel.entity.ApiEquipmentFormMap;
import com.ysttench.application.equip.settings.kernel.entity.ApiEquipmentSpareFormMap;
import com.ysttench.application.equip.settings.kernel.entity.ApiExceptFormMap;
import com.ysttench.application.equip.settings.kernel.entity.ApiGrainFormMap;
import com.ysttench.application.equip.settings.kernel.entity.ApiMntenceFormMap;
import com.ysttench.application.equip.settings.kernel.entity.ApiSpareFormMap;
import com.ysttench.application.equip.settings.kernel.mapper.ApiEquipmentMapper;
import com.ysttench.application.equip.settings.web.controller.common.AttrConstants;
import com.ysttench.application.equip.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.equip.settings.web.controller.index.BaseController;

/**
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/apiequip/")
public class ApiequipController extends BaseController {
	@Inject
	ApiEquipmentMapper apiEquipmentMapper;

	/**
	 * 跳转到设备信息录入界面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	public String listUI(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.EQUIP + ForwardConstants.LIST;
	}

	/**
	 * 跳转到设备记录信息页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("cklist")
	public String listckUI(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.CKEQUIP + ForwardConstants.LIST;
	}

	/**
	 * 信息录入界面
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
		ApiEquipmentFormMap apiEquipmentFormMap = getFormMap(ApiEquipmentFormMap.class);
		apiEquipmentFormMap = toFormMap(apiEquipmentFormMap, pageNow, pageSize, apiEquipmentFormMap.getStr("orderby"));
		apiEquipmentFormMap.put("column", StringUtil.prop2tablefield(column));
		apiEquipmentFormMap.put("sort", sort);
		apiEquipmentFormMap.put("userName", SessionUtil.getSessionAttr("userName").toString());
		pageView.setRecords(apiEquipmentMapper.findEquipmentPage(apiEquipmentFormMap));
		return pageView;
	}

	/**
	 * 设备记录信息页面
	 * 
	 * @param pageNow
	 * @param pageSize
	 * @param column
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("findckByPage")
	public PageView findckByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
		ApiEquipmentFormMap apiEquipmentFormMap = getFormMap(ApiEquipmentFormMap.class);
		apiEquipmentFormMap = toFormMap(apiEquipmentFormMap, pageNow, pageSize, apiEquipmentFormMap.getStr("orderby"));
		apiEquipmentFormMap.put("column", StringUtil.prop2tablefield(column));
		apiEquipmentFormMap.put("sort", sort);
		apiEquipmentFormMap.put("userName", SessionUtil.getSessionAttr("userName").toString());
		pageView.setRecords(apiEquipmentMapper.findCkEquipmentPage(apiEquipmentFormMap));
		return pageView;
	}

	/**
	 * 进入设备新增页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("addUI")
	public String addUI(Model model) throws Exception {
		return ForwardConstants.API + ForwardConstants.EQUIP + ForwardConstants.ADD;
	}

	/**
	 * 进入设备新增页面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("addEntity")
	@SystemLog(module = "设备管理", methods = "设备管理-新增设备") // 凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly = false) // 需要事务操作必须加入此注解
	public String addEntity(String txtGroupsSelect) {
		try {
			if (txtGroupsSelect == null || txtGroupsSelect.equals("")) {
				return AttrConstants.FAIL;
			} else {
				ApiEquipmentFormMap apiEquipmentFormMap = getFormMap(ApiEquipmentFormMap.class);
				apiEquipmentFormMap.put("entryName", SessionUtil.getSessionAttr("userName").toString());
				apiEquipmentMapper.addEntity(apiEquipmentFormMap);
				if (!StringUtil.isEmpty(txtGroupsSelect)) {
					String[] txt = txtGroupsSelect.split(",");
					ApiEquipmentSpareFormMap apiEquipmentSpareFormMap = new ApiEquipmentSpareFormMap();
					for (String spareId : txt) {
						apiEquipmentSpareFormMap.put("equipId", apiEquipmentFormMap.get("id"));
						apiEquipmentSpareFormMap.put("spareId", spareId);
						apiEquipmentMapper.addEntityru(apiEquipmentSpareFormMap);
					}
				}
			}
		} catch (Exception e) {
			throw new SystemException("添加设备异常");
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
	@SystemLog(module = "设备管理", methods = "设备管理-删除设备")
	// 凡需要处理业务逻辑的.都需要记录操作日志
	public String deleteEntity() throws Exception {
		String[] ids = SessionUtil.getParaValues("ids");
		String[] ids1 = ids[0].split(",");
		for (String id : ids1) {
			ApiEquipmentSpareFormMap apiEquipmentSpareFormMap = new ApiEquipmentSpareFormMap();
			apiEquipmentSpareFormMap.put("equipId", id);
			apiEquipmentMapper.deleteEquipmentSpare(apiEquipmentSpareFormMap);
			ApiEquipmentFormMap apiEquipmentFormMap = new ApiEquipmentFormMap();
			apiEquipmentFormMap.put("id", id);
			apiEquipmentFormMap.put("status", "1");
			apiEquipmentMapper.editEquipment(apiEquipmentFormMap);
		}
		return AttrConstants.SUCCESS;
	}

	/**
	 * 进入设备编辑画面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("editUI")
	public String editUI(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		if (StringUtil.isNotEmpty(id)) {
			ApiEquipmentFormMap apiEquipmentFormMap = new ApiEquipmentFormMap();
			apiEquipmentFormMap.put("id", id);
			List<ApiEquipmentFormMap> list = apiEquipmentMapper.findDetailEquip(apiEquipmentFormMap);
			model.addAttribute("equipment", list.get(0));
		}
		return ForwardConstants.API + ForwardConstants.EQUIP + ForwardConstants.EDIT;
	}

	/**
	 * 进入配件列表画面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("spareUI")
	public String spareUI(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		model.addAttribute("equip", id);
		return ForwardConstants.API + ForwardConstants.EQUIP + ForwardConstants.SPARELIST;
	}
	/**
	 * 进入稼动信息列表画面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("grainUI")
	public String grainUI(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		model.addAttribute("equip", id);
		return ForwardConstants.API + ForwardConstants.EQUIP + ForwardConstants.GAINLIST;
	}
	/**
	 * 稼动信息列表画面
	 * @param pageNow
	 * @param pageSize
	 * @param column
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	 @ResponseBody
	    @RequestMapping("findgainByPage")
	    public PageView findgainByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
		 String equipid = SessionUtil.getPara("equipid");
		 ApiGrainFormMap apiGrainFormMap = getFormMap(ApiGrainFormMap.class);
		 apiGrainFormMap = toFormMap(apiGrainFormMap, pageNow, pageSize, apiGrainFormMap.getStr("orderby"));
		 apiGrainFormMap.put("column", StringUtil.prop2tablefield(column));
		 apiGrainFormMap.put("sort", sort);
		 apiGrainFormMap.put("equipid", equipid);
		 apiGrainFormMap.put("userName", SessionUtil.getSessionAttr("userName").toString());
	        pageView.setRecords(apiEquipmentMapper.findgainByPage(apiGrainFormMap));
	        return pageView;
	    }
	/**
	 * 进入维护信息列表画面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("mntenceUI")
	public String mntenceUI(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		model.addAttribute("equip", id);
		return ForwardConstants.API + ForwardConstants.EQUIP + ForwardConstants.MNTENCELIST;
	}
	/**
	 * 维护信息列表画面
	 * @param pageNow
	 * @param pageSize
	 * @param column
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
    @RequestMapping("findmntenceByPage")
    public PageView findmntenceByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
	 String equipid = SessionUtil.getPara("equipid");
	 ApiMntenceFormMap apiMntenceFormMap= getFormMap(ApiMntenceFormMap.class);
	 apiMntenceFormMap = toFormMap(apiMntenceFormMap, pageNow, pageSize, apiMntenceFormMap.getStr("orderby"));
	 apiMntenceFormMap.put("column", StringUtil.prop2tablefield(column));
	 apiMntenceFormMap.put("sort", sort);
	 apiMntenceFormMap.put("equipid", equipid);
	 apiMntenceFormMap.put("userName", SessionUtil.getSessionAttr("userName").toString());
        pageView.setRecords(apiEquipmentMapper.findmntenceByPage(apiMntenceFormMap));
        return pageView;
    }
	/**
	 * 进入异常列表画面
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("exceptUI")
	public String exceptUI(Model model) throws Exception {
		String id = SessionUtil.getPara("id");
		model.addAttribute("equip", id);
		return ForwardConstants.API + ForwardConstants.EQUIP + ForwardConstants.EXCEPTLIST;
	}
	/**
	 * 异常列表画面
	 * @param pageNow
	 * @param pageSize
	 * @param column
	 * @param sort
	 * @return
	 * @throws Exception
	 */
	 @ResponseBody
	    @RequestMapping("findExceptByPage")
	    public PageView findExceptByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
		 String equipid = SessionUtil.getPara("equipid");
		 ApiExceptFormMap apiExceptFormMap = getFormMap(ApiExceptFormMap.class);
		 apiExceptFormMap = toFormMap(apiExceptFormMap, pageNow, pageSize, apiExceptFormMap.getStr("orderby"));
	        apiExceptFormMap.put("column", StringUtil.prop2tablefield(column));
	        apiExceptFormMap.put("sort", sort);
	        apiExceptFormMap.put("equipid", equipid);
	        apiExceptFormMap.put("userName", SessionUtil.getSessionAttr("userName").toString());
	        pageView.setRecords(apiEquipmentMapper.findExceptByPage(apiExceptFormMap));
	        return pageView;
	    }

	/**
	 * 设备编辑保存
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("editEntity")
	@SystemLog(module = "设备管理", methods = "设备管理-编辑设备") // 凡需要处理业务逻辑的.都需要记录操作日志
	@Transactional(readOnly = false) // 需要事务操作必须加入此注解
	public String editEntity(String txtGroupsSelect) {
		try {
			if (txtGroupsSelect == null || txtGroupsSelect.equals("")) {
				return AttrConstants.FAIL;
			} else {
				ApiEquipmentFormMap apiEquipmentFormMap = getFormMap(ApiEquipmentFormMap.class);
				apiEquipmentMapper.editEntity(apiEquipmentFormMap);
				if (!StringUtil.isEmpty(txtGroupsSelect)) {
					ApiEquipmentSpareFormMap apiEquipmentSpareFormMap = new ApiEquipmentSpareFormMap();
					apiEquipmentSpareFormMap.put("equipId", apiEquipmentFormMap.get("id").toString());
					apiEquipmentMapper.deleteEquipmentSpare(apiEquipmentSpareFormMap);
					String[] txt = txtGroupsSelect.split(",");
					for (String spareId : txt) {
						ApiEquipmentSpareFormMap equipment = new ApiEquipmentSpareFormMap();
						equipment.put("equipId", apiEquipmentFormMap.get("id"));
						equipment.put("spareId", spareId);
						apiEquipmentMapper.addEntityru(equipment);
					}
				}
			}
		} catch (Exception e) {
			throw new SystemException("编辑设备异常");
		}
		return AttrConstants.SUCCESS;
	}
}