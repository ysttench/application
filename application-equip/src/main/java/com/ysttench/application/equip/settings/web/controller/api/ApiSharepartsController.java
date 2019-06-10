package com.ysttench.application.equip.settings.web.controller.api;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.exception.SystemException;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.BeanUtil;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.equip.settings.kernel.entity.ApiEquipmentFormMap;
import com.ysttench.application.equip.settings.kernel.entity.ApiEquipmentSpareFormMap;
import com.ysttench.application.equip.settings.kernel.entity.ApiSpareFormMap;
import com.ysttench.application.equip.settings.kernel.entity.SysRoleFormMap;
import com.ysttench.application.equip.settings.kernel.entity.SysUserFormMap;
import com.ysttench.application.equip.settings.kernel.entity.SysUserRoleFormMap;
import com.ysttench.application.equip.settings.kernel.mapper.ApiSpareMapper;
import com.ysttench.application.equip.settings.web.controller.common.AttrConstants;
import com.ysttench.application.equip.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.equip.settings.web.controller.index.BaseController;
import com.ysttench.application.equip.settings.web.rdto.system.SysUserBean;
/**
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/apispare/")
public class ApiSharepartsController extends BaseController{
	@Inject
	ApiSpareMapper apiSpareMapper;
	/**
	 * 跳转到零件信息管理页面
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.API + ForwardConstants.SPARE + ForwardConstants.LIST;
    }
	/**
	 * 配件信息界面
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
        ApiSpareFormMap apiSpareFormMap = getFormMap(ApiSpareFormMap.class);
        apiSpareFormMap = toFormMap(apiSpareFormMap, pageNow, pageSize, apiSpareFormMap.getStr("orderby"));
        apiSpareFormMap.put("column", StringUtil.prop2tablefield(column));
        apiSpareFormMap.put("sort", sort);
        apiSpareFormMap.put("userName", SessionUtil.getSessionAttr("userName").toString());
        pageView.setRecords(apiSpareMapper.findSparePage(apiSpareFormMap));
        return pageView;
    }
    /**
	 * 配件信息界面
	 * @param pageNow
	 * @param pageSize
	 * @param column
	 * @param sort
	 * @return
	 * @throws Exception
	 */
    @ResponseBody
    @RequestMapping("findByPage2")
    public PageView findByPage2(String pageNow, String pageSize, String column, String sort) throws Exception {
    	String equipid = SessionUtil.getPara("equipid");
        ApiSpareFormMap apiSpareFormMap = getFormMap(ApiSpareFormMap.class);
        apiSpareFormMap = toFormMap(apiSpareFormMap, pageNow, pageSize, apiSpareFormMap.getStr("orderby"));
        apiSpareFormMap.put("column", StringUtil.prop2tablefield(column));
        apiSpareFormMap.put("sort", sort);
        apiSpareFormMap.put("equipid", equipid);
        apiSpareFormMap.put("userName", SessionUtil.getSessionAttr("userName").toString());
        pageView.setRecords(apiSpareMapper.findSparePage2(apiSpareFormMap));
        return pageView;
    }
    /**
     * 删除配件
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("spareTimeStart")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "设备管理", methods = "配件管理-配件启用")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String spareTimeStart() throws Exception {
    	try {
    		String[] ids = SessionUtil.getParaValues("ids");
    		String[] ids1 = ids[0].split(",");
    		for (String id : ids1) {
    			ApiEquipmentSpareFormMap apiEquipmentSpareFormMap = new ApiEquipmentSpareFormMap();
    			apiEquipmentSpareFormMap.put("id", id);
    			apiEquipmentSpareFormMap.put("enableTime", DatetimeUtil.getDateyyyyMMdd());
    			apiSpareMapper.editEquimentSpare(apiEquipmentSpareFormMap);
    		}
		} catch (Exception e) {
			 throw new SystemException("启用配件异常");
		}
        return AttrConstants.SUCCESS;
    }
    /**
     * 配件新增
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("addUI")
    public String addUI(Model model) throws Exception {
        return ForwardConstants.API + ForwardConstants.SPARE + ForwardConstants.ADD;
    }
    /**
     * 配件新增
     * @return
     */
    @ResponseBody
    @RequestMapping("addEntity")
    @SystemLog(module = "配件管理", methods = "配件管理-新增配件") // 凡需要处理业务逻辑的.都需要记录操作日志
    @Transactional(readOnly = false) // 需要事务操作必须加入此注解
    public String addEntity() {
        try {
        	ApiSpareFormMap apiSpareFormMap = getFormMap(ApiSpareFormMap.class);
        	apiSpareFormMap.put("entryName", SessionUtil.getSessionAttr("userName").toString());
        	apiSpareMapper.addEntity(apiSpareFormMap);
        } catch (Exception e) {
            throw new SystemException("添加配件异常");
        }
        return AttrConstants.SUCCESS;
    }
    /**
     * 删除配件
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "设备管理", methods = "配件管理-删除配件")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String deleteEntity() throws Exception {
    	try {
    		String[] ids = SessionUtil.getParaValues("ids");
    		String[] ids1 = ids[0].split(",");
    		for (String id : ids1) {
    			ApiSpareFormMap apiSpareFormMap = new ApiSpareFormMap();
    			apiSpareFormMap.put("id", id);
    			apiSpareFormMap.put("status", "1");
    			apiSpareMapper.editEntity1(apiSpareFormMap);
    		}
		} catch (Exception e) {
			 throw new SystemException("删除配件异常");
		}
        return AttrConstants.SUCCESS;
    }
    /**
     * 进入配件编辑画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
        String id = SessionUtil.getPara("id");
        if (StringUtil.isNotEmpty(id)) {
        	ApiSpareFormMap apiSpareFormMap = new ApiSpareFormMap();
        	apiSpareFormMap.put("id", id);
        	List<ApiSpareFormMap> list = apiSpareMapper.findDetailSpare(apiSpareFormMap);
        	model.addAttribute("spare", list.get(0));
        }
        return ForwardConstants.API + ForwardConstants.SPARE + ForwardConstants.EDIT;
    }
    
    /**
     * 进入配件编辑画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("spareNumchange")
    public String spareNumchange(Model model) throws Exception {
        String id = SessionUtil.getPara("id");
        if (StringUtil.isNotEmpty(id)) {
        	ApiEquipmentSpareFormMap apiEquipmentSpareFormMap = new ApiEquipmentSpareFormMap();
        	apiEquipmentSpareFormMap.put("id", id);
        	List<ApiEquipmentSpareFormMap> list = apiSpareMapper.findDetailEquipmentSpare(apiEquipmentSpareFormMap);
        	model.addAttribute("spare", list.get(0));
        }
        return ForwardConstants.API + ForwardConstants.SPARE + ForwardConstants.SPARE_EDIT;
    }
    /**
     * 编辑配件保存
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editNum")
    @SystemLog(module = "设备管理", methods = "配件管理-修改配件数量")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String editNum(String txtGroupsSelect) throws Exception {
    try {
    	ApiEquipmentSpareFormMap apiEquipmentSpareFormMap  = getFormMap(ApiEquipmentSpareFormMap.class);
    	apiSpareMapper.editNum(apiEquipmentSpareFormMap);
    } catch (Exception e) {
        throw new SystemException("修改配件异常");
    }
    return AttrConstants.SUCCESS;
}
    
    /**
     * 编辑配件保存
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    @SystemLog(module = "设备管理", methods = "配件管理-修改配件")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String editEntity(String txtGroupsSelect) throws Exception {
    try {
    	ApiSpareFormMap apiSpareFormMap = getFormMap(ApiSpareFormMap.class);
    	apiSpareMapper.editEntity(apiSpareFormMap);
    } catch (Exception e) {
        throw new SystemException("修改配件异常");
    }
    return AttrConstants.SUCCESS;
}
    /**
     * 获取配件下拉
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("selSpare")
    public String seletSpare(Model model) throws Exception {
    	ApiSpareFormMap apiSpareFormMap = getFormMap(ApiSpareFormMap.class);
    	 Object equipId = apiSpareFormMap.get("equipId");
    	 if (null != equipId) {
             List<ApiSpareFormMap> list = apiSpareMapper.seletEquipSpare(apiSpareFormMap);
             String ugid = "";
             for (ApiSpareFormMap ml : list) {
                 ugid += ml.get("id") + ",";
             }
             ugid = StringUtil.removeEnd(ugid);
             model.addAttribute("txtSpareSelect", ugid);
             model.addAttribute("equipSpare", list);
             if (StringUtils.isNotBlank(ugid)) {
            	 apiSpareFormMap.put("id", ugid);
             }
             List<ApiSpareFormMap> spares = apiSpareMapper.findByWhere(apiSpareFormMap);
             model.addAttribute("spare", spares);
             return ForwardConstants.API + ForwardConstants.SPARE + ForwardConstants.SPARE_SELECT;
         } else {
             List<ApiSpareFormMap> spares = apiSpareMapper.findByWhere(apiSpareFormMap);
             model.addAttribute("spare", spares);
             return ForwardConstants.API + ForwardConstants.SPARE + ForwardConstants.SPARE_SELECT;
         }

    }
}
