package com.ysttench.application.htdytasties.settings.web.controller.system;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.htdytasties.settings.kernel.entity.SysRoleFormMap;
import com.ysttench.application.htdytasties.settings.kernel.entity.SysUserFormMap;
import com.ysttench.application.htdytasties.settings.kernel.mapper.SysRoleMapper;
import com.ysttench.application.htdytasties.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htdytasties.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htdytasties.settings.web.controller.index.BaseController;

/**
 * 
 * @author howard
 */
@Controller
@RequestMapping("/role/")
public class SysRoleController extends BaseController {
    static String all = "all";
    static String state = "state";
    static String admin = "admin";
    static String username = "userName";
    @Inject
    private SysRoleMapper sysRoleMapper;

    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        // model.addAttribute("res", findByRes());
        return ForwardConstants.SYSTEM + ForwardConstants.ROLE + ForwardConstants.LIST;
    }

    @ResponseBody
    @RequestMapping("findByPage")
    public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
        SysRoleFormMap sysRoleFormMap = getFormMap(SysRoleFormMap.class);
        if (!sysRoleFormMap.isEmpty()) {
            if (all.equals(sysRoleFormMap.getStr(state))) {
                sysRoleFormMap.remove("state");
            }
        }
        if (!admin.equals(((SysUserFormMap) SessionUtil.getUserSession()).getStr(username))) {
            sysRoleFormMap.put("where", " and NAME <> '系统管理员'");
        }
        sysRoleFormMap = toFormMap(sysRoleFormMap, pageNow, pageSize, sysRoleFormMap.getStr("orderby"));
        sysRoleFormMap.put("column", StringUtil.prop2tablefield(column));
        sysRoleFormMap.put("sort", sort);
        pageView.setRecords(sysRoleMapper.findRolePage(sysRoleFormMap));
        return pageView;
    }

    @RequestMapping("addUI")
    public String addUI(Model model) throws Exception {
        return ForwardConstants.SYSTEM + ForwardConstants.ROLE + ForwardConstants.ADD;
    }

    @ResponseBody
    @RequestMapping("addEntity")
    @Transactional(readOnly = false)
    /** 需要事务操作必须加入此注解*/
    @SystemLog(module = "系统管理", methods = "角色管理-新增角色")
    /** 凡需要处理业务逻辑的.都需要记录操作日志*/
    public String addEntity() throws Exception {
        SysRoleFormMap sysRoleFormMap = getFormMap(SysRoleFormMap.class);
        sysRoleMapper.addEntity(sysRoleFormMap);
        return AttrConstants.SUCCESS;
    }

    @ResponseBody
    @RequestMapping("deleteEntity")
    @Transactional(readOnly = false)
    /** 需要事务操作必须加入此注解*/
    @SystemLog(module = "系统管理", methods = "角色管理-删除角色")
    /** 凡需要处理业务逻辑的.都需要记录操作日志*/
    public String deleteEntity() throws Exception {
        String[] ids = SessionUtil.getParaValues("ids");
        String[] ids1 = ids[0].split(",");
        for (String id : ids1) {
            sysRoleMapper.deleteByAttribute("roleId", id,SysRoleFormMap.class);
        }
        return AttrConstants.SUCCESS;
    }

    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
        String id = SessionUtil.getPara("id");
        if (StringUtil.isNotEmpty(id)) {
            model.addAttribute("role", sysRoleMapper.findbyFrist("roleId", id, SysRoleFormMap.class));
        }
        return ForwardConstants.SYSTEM + ForwardConstants.ROLE + ForwardConstants.EDIT;
    }

    @ResponseBody
    @RequestMapping("editEntity")
    @Transactional(readOnly = false)
    /** 需要事务操作必须加入此注解*/
    @SystemLog(module = "系统管理", methods = "角色管理-修改角色")
    /** 凡需要处理业务逻辑的.都需要记录操作日志*/
    public String editEntity() throws Exception {
        SysRoleFormMap sysRoleFormMap = getFormMap(SysRoleFormMap.class);
        sysRoleMapper.editEntity(sysRoleFormMap);
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
        SysRoleFormMap sysRoleFormMap = getFormMap(SysRoleFormMap.class);
        Object userId = sysRoleFormMap.get("userId");
        if (null != userId) {
            List<SysRoleFormMap> list = sysRoleMapper.seletUserRole(sysRoleFormMap);
            String ugid = "";
            for (SysRoleFormMap ml : list) {
                ugid += ml.get("roleId") + ",";
            }
            ugid = StringUtil.removeEnd(ugid);
            model.addAttribute("txtRoleSelect", ugid);
            model.addAttribute("userRole", list);
            if (StringUtils.isNotBlank(ugid)) {
                sysRoleFormMap.put("where", " where ROLE_ID not in (" + ugid + ") AND STATE = 0");
            }
            List<SysRoleFormMap> roles = sysRoleMapper.findByWhere(sysRoleFormMap);
            model.addAttribute("role", roles);
            return ForwardConstants.SYSTEM + ForwardConstants.USER + ForwardConstants.ROLE_SELECT;
        } else {
            List<SysRoleFormMap> roles = sysRoleMapper.findByWhere(sysRoleFormMap);
            model.addAttribute("role", roles);
            return ForwardConstants.SYSTEM + ForwardConstants.USER + ForwardConstants.ROLE_SELECT;
        }

    }

    /**
     * 验证角色是否存在
     * 
     * @param roleName
     * @return
     */
    @RequestMapping("isExist")
    @ResponseBody
    public boolean isExist(String roleId,String name) {
        SysRoleFormMap sysRoleFormMap = new SysRoleFormMap();
        sysRoleFormMap.put("roleId", roleId);
        sysRoleFormMap.put("name", name);
        List<SysRoleFormMap> role = sysRoleMapper.count(sysRoleFormMap);
        int count = Integer.parseInt(role.get(0).get("count").toString());
        if (count > 0) {
            return false;
        } else {
            return true;
        }
    }

}