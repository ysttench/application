package com.ysttench.application.htbw.settings.web.controller.system;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.htbw.settings.common.TreeUtil;
import com.ysttench.application.htbw.settings.kernel.entity.SysMenuFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.SysRightConfigFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.SysMenuMapper;
import com.ysttench.application.htbw.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htbw.settings.web.controller.index.BaseController;
import com.ysttench.application.htbw.settings.web.rdto.MenuTreeObject;
import com.ysttench.application.htbw.settings.web.rdto.MenuTreeObjectUtil;

/**
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/right/")
public class SysRightConfigController extends BaseController {
    @Inject
    private SysMenuMapper sysMenuMapper;

    /**
     * 权限分配页面
     * 
     * @param model
     * @return
     */
    @RequestMapping("menuRight")
    public String permissions(Model model) {
	String roleId = SessionUtil.getPara("roleId");
	SysMenuFormMap sysMenuFormMap = getFormMap(SysMenuFormMap.class);
	sysMenuFormMap.put("where", "where IS_HIDE = 0 order by MENU_LEVEL asc");
	List<SysMenuFormMap> mps = sysMenuMapper.findByWhere(sysMenuFormMap);
	List<MenuTreeObject> list = new ArrayList<MenuTreeObject>();
	for (SysMenuFormMap map : mps) {
	    MenuTreeObject ts = new MenuTreeObject();
	    MenuTreeObjectUtil.map2Tree(ts, map, "menuId");
	    list.add(ts);
	}
	TreeUtil treeUtil = new TreeUtil();
	List<MenuTreeObject> ns = treeUtil.getChildTreeObjects(list, 0);
	model.addAttribute("permissions", ns);
	model.addAttribute("roleId", roleId);
	return ForwardConstants.SYSTEM + ForwardConstants.MENU + ForwardConstants.MENU_RIGHT;
    }

    @ResponseBody
    @RequestMapping("addMenuRight")
    @Transactional(readOnly = false) /** 需要事务操作必须加入此注解 */
    @SystemLog(module = "系统管理", methods = "用户管理/菜单权限管理-修改权限") /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    public String addUserRes() throws Exception {
	String roleId = SessionUtil.getPara("roleId");
	sysMenuMapper.deleteByAttribute("roleId", roleId, SysRightConfigFormMap.class);
	String[] menuIds = SessionUtil.getParaValues("menuId[]");
	if (menuIds != null && menuIds.length != 0 && menuIds[0] != null) {
	    for (String menuId : menuIds) {
		SysRightConfigFormMap sysRightConfigFormMap2 = new SysRightConfigFormMap();
		sysRightConfigFormMap2.put("rightId", menuId);
		sysRightConfigFormMap2.put("roleId", roleId);
		sysRightConfigFormMap2.put("resourceFlag", '0');
		sysMenuMapper.addEntity(sysRightConfigFormMap2);

	    }
	}
	return AttrConstants.SUCCESS;
    }

}
