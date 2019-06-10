package com.ysttench.application.auth.settings.web.controller.api;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.auth.settings.kernel.entity.ApiRoleFormMap;
import com.ysttench.application.auth.settings.kernel.entity.ApiSystemInfoFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.ApiRoleMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.exception.SystemException;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.StringUtil;

@Controller
@RequestMapping("/apiRole/")
public class ApiRoleController extends BaseController {
    @Inject
    private ApiRoleMapper apiRoleMapper;

    /**
     * 打开角色列表画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.API+ForwardConstants.ROLE+ForwardConstants.LIST;
    }

    /**
     * 角色列表分页处理
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
        ApiRoleFormMap apiRoleFormMap = getFormMap(ApiRoleFormMap.class);
        if (!apiRoleFormMap.isEmpty()) {
            if ("all".equals(apiRoleFormMap.getStr("applyId"))) {
                apiRoleFormMap.remove("applyId");
            }
        }
        apiRoleFormMap = toFormMap(apiRoleFormMap, pageNow, pageSize, apiRoleFormMap.getStr("orderby"));
        apiRoleFormMap.put("column", StringUtil.prop2tablefield(column));
        apiRoleFormMap.put("sort", sort);
        pageView.setRecords(apiRoleMapper.findRoleByPage(apiRoleFormMap));
        return pageView;
    }

    /**
     * 打开角色新增画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("addUI")
    public String addUI(Model model) throws Exception {
        return ForwardConstants.API+ForwardConstants.ROLE+ForwardConstants.ADD;
    }

    /**
     * 保存角色信息
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("addEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "应用管理", methods = "角色管理-新增角色")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String addEntity() throws Exception {
        ApiRoleFormMap apiRoleFormMap = getFormMap(ApiRoleFormMap.class);
        try {
            if (!apiRoleFormMap.isEmpty()) {
                if ("all".equals(apiRoleFormMap.getStr("applyId"))) {
                    return AttrConstants.FAIL;
                }
            }
            apiRoleFormMap.put("createTime", DatetimeUtil.fromDateH());
            apiRoleFormMap.put("updateTime", DatetimeUtil.fromDateH());
            apiRoleMapper.addEntity(apiRoleFormMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加角色异常");
            throw new SystemException("添加账号异常");
        }
        return AttrConstants.SUCCESS;
    }

    /**
     * 角色删除操作
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "应用管理", methods = "角色管理-删除角色")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String deleteEntity() {
        String[] ids = SessionUtil.getPara("ids").split(",");
        for (String id : ids) {
            List<ApiRoleFormMap> list = apiRoleMapper.findByAttribute("ROLE_ID", id, ApiRoleFormMap.class);
            if (list != null && !"".equals(list)) {
                if (list.get(0).get("deletedFlag").equals("1")) {
                    return AttrConstants.FAIL;
                } else {
                        ApiRoleFormMap apiRoleFormMap = new ApiRoleFormMap();
                        apiRoleFormMap.put("roleId", id);
                        apiRoleFormMap.put("deletedFlag", "1");
                        try {
                            apiRoleMapper.editEntity(apiRoleFormMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("删除角色异常");
                            return AttrConstants.FAIL;
                        }
                }
            }
        }
        return AttrConstants.SUCCESS;
    }

    /**
     * 打开角色编辑画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
        String id = SessionUtil.getPara("id");
        if (StringUtil.isNotEmpty(id)) {
            ApiRoleFormMap apiRoleFormMap = new ApiRoleFormMap();
            apiRoleFormMap.put("roleId", id);
            model.addAttribute("role", apiRoleMapper.findDetail(apiRoleFormMap).get(0));
            model.addAttribute("yyxtlist",apiRoleMapper.findByWhere(new ApiSystemInfoFormMap()));
        }
        return ForwardConstants.API+ForwardConstants.ROLE+ForwardConstants.EDIT;
    }

    /**
     * 角色编辑保存
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "应用管理", methods = "角色管理-修改角色")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String editEntity() throws Exception {
        ApiRoleFormMap apiRoleFormMap = getFormMap(ApiRoleFormMap.class);
        apiRoleFormMap.put("updateTime", DatetimeUtil.getDate());
        try {
            apiRoleMapper.editEntity(apiRoleFormMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新角色异常");
            return AttrConstants.FAIL;
        }
        return AttrConstants.SUCCESS;
    }

    /**
     * 获取角色下拉框
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("selRole")
    public String seletRole(Model model) throws Exception {
        ApiRoleFormMap apiRoleFormMap = getFormMap(ApiRoleFormMap.class);
        Object userId = apiRoleFormMap.get("userId");
        if (null != userId) {
            List<ApiRoleFormMap> list = apiRoleMapper.seletUserRole(apiRoleFormMap);
            String ugid = "";
            for (ApiRoleFormMap ml : list) {
                ugid += ml.get("roleId") + ",";
            }
            ugid = StringUtil.removeEnd(ugid);
            model.addAttribute("txtRoleSelect", ugid);
            model.addAttribute("userRole", list);
            if (StringUtils.isNotBlank(ugid)) {
                apiRoleFormMap.put("where", " where ROLE_ID not in (" + ugid + ") and DELETED_FLAG = 0");
            }
            apiRoleFormMap.put("where", "where DELETED_FLAG = 0");
            List<ApiRoleFormMap> roles = apiRoleMapper.findByWhere(apiRoleFormMap);
            model.addAttribute("role", roles);
        } else {
            ApiRoleFormMap apiRoleFormMap2 = new ApiRoleFormMap();
            apiRoleFormMap2.put("where", "where DELETED_FLAG = 0 ");
            List<ApiRoleFormMap> roles = apiRoleMapper.findByWhere(apiRoleFormMap2);
            model.addAttribute("role", roles);
        }
        return ForwardConstants.API+ForwardConstants.USER+ForwardConstants.ROLE_SELECT;
    }

    /**
     * 验证角色是否存在
     * 
     * @param roleName
     * @return
     */
    @RequestMapping("isExist")
    @ResponseBody
    public boolean isExist(String id,String roleName) {
        ApiRoleFormMap apiRoleFormMap = new ApiRoleFormMap();
        apiRoleFormMap.put("id", id);
        apiRoleFormMap.put("roleName", roleName);
        List<ApiRoleFormMap> role = apiRoleMapper.count(apiRoleFormMap);
        int count = Integer.parseInt(role.get(0).get("count").toString());
        if (count > 0) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 获取应用系统信息 
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("findAllXT")
    public List<ApiSystemInfoFormMap> findAllXT() {
        return apiRoleMapper.findByWhere(new ApiSystemInfoFormMap());
    }
}