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
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.htbw.settings.common.TreeUtil;
import com.ysttench.application.htbw.settings.kernel.entity.SysMenuFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.SysMenuMapper;
import com.ysttench.application.htbw.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htbw.settings.web.controller.index.BaseController;
import com.ysttench.application.htbw.settings.web.rdto.MenuTreeObject;
import com.ysttench.application.htbw.settings.web.rdto.MenuTreeObjectUtil;
import com.ysttench.application.htbw.settings.web.rdto.Params;

/**
 * 
 * @author howard
 */
@Controller
@RequestMapping("/menu/")
public class SysMenuController extends BaseController {
    static String name="name";
    static String parentId="parentId";
    static String num="0";
    @Inject
    private SysMenuMapper sysMenuMapper;

    /**
     * @param model 存放返回界面的model
     * @return
     */
    @ResponseBody
    @RequestMapping("treelists")
    public SysMenuFormMap findByPage(Model model) {
        SysMenuFormMap sysMenuFormMap = getFormMap(SysMenuFormMap.class);
        String order = " order by MENU_LEVEL asc";
        sysMenuFormMap.put("orderby", order);
        List<SysMenuFormMap> mps = null;
        if (sysMenuFormMap.size() > 1) {
            SysMenuFormMap sys = new SysMenuFormMap();
            sys.put("orderby", order);
            if (!StringUtil.isEmpty(sysMenuFormMap.getStr(name))) {
                List<SysMenuFormMap> list = sysMenuMapper.findByAttribute("Name",
                        sysMenuFormMap.getStr(name), SysMenuFormMap.class);
                if (list.get(0).get(parentId).toString().equals(num)) {
                    sys.put("where", "where PARENT_ID = '" + list.get(0).get("menuId").toString()
                            + "' or MENU_ID = '" + list.get(0).get("menuId").toString() + "'");
                } else {
                    sys.put("where", "where MENU_ID in ( '" + list.get(0).get("parentId").toString() + "','"
                            + list.get(0).get("menuId").toString() + "' )");
                }
                mps = sysMenuMapper.findByWhere(sys);
            } else {
                mps = sysMenuMapper.findByNames(sys);
            }
        } else {
            mps = sysMenuMapper.findByNames(sysMenuFormMap);
        }
        List<MenuTreeObject> list = new ArrayList<MenuTreeObject>();
        for (SysMenuFormMap map : mps) {
            MenuTreeObject ts = new MenuTreeObject();
            MenuTreeObjectUtil.map2Tree(ts, map, "menuId");
            list.add(ts);
        }
        TreeUtil treeUtil = new TreeUtil();
        List<MenuTreeObject> ns = treeUtil.getChildTreeObjects(list, 0);
        sysMenuFormMap = new SysMenuFormMap();
        sysMenuFormMap.put("treelists", ns);
        return sysMenuFormMap;
    }
    /**
     * 菜单下拉框
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("menulists")
    public List<MenuTreeObject> reslists(Model model) throws Exception {
	SysMenuFormMap sysMenuFormMap = getFormMap(SysMenuFormMap.class);
	List<SysMenuFormMap> mps = sysMenuMapper.findByWhere(sysMenuFormMap);
	List<MenuTreeObject> list = new ArrayList<MenuTreeObject>();
	for (SysMenuFormMap map : mps) {
	    MenuTreeObject ts = new MenuTreeObject();
	    MenuTreeObjectUtil.map2Tree(ts, map, "menuId");
	    list.add(ts);
	}
	TreeUtil treeUtil = new TreeUtil();
	List<MenuTreeObject> ns = treeUtil.getChildTreeObjects(list, 0, "　");
	return ns;
    }

    /**
     * @param model 存放返回界面的model
     * @return
     */
    @RequestMapping("list")
    public String list(Model model) {
	return ForwardConstants.SYSTEM + ForwardConstants.MENU + ForwardConstants.LIST;
    }

    /**
     * 跳转到修改界面
     * 
     * @param model
     * @param menuId 修改菜单信息ID
     * @return
     */
    @RequestMapping("editUI")
    public String editUI(Model model) {
	String id = SessionUtil.getPara("id");
	if (StringUtil.isNotEmpty(id)) {
	     model.addAttribute("menu", sysMenuMapper.findbyFrist("menuId", id,
	     SysMenuFormMap.class));
	}
	return ForwardConstants.SYSTEM + ForwardConstants.MENU + ForwardConstants.EDIT;
    }

    /**
     * 跳转到新增界面
     * 
     * @return
     */
    @RequestMapping("addUI")
    public String addUI(Model model) {
	return ForwardConstants.SYSTEM + ForwardConstants.MENU + ForwardConstants.ADD;
    }

    /**
     * 添加菜单
     * 
     * @param menu
     * @return Map
     * @throws Exception
     */
    @RequestMapping("addEntity")
    @ResponseBody
    @Transactional(readOnly = false) /** 需要事务操作必须加入此注解 */
    @SystemLog(module = "系统管理", methods = "菜单管理-新增资源") /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    public String addEntity() throws Exception {
	    SysMenuFormMap sysMenuFormMap = getFormMap(SysMenuFormMap.class);
	    sysMenuMapper.addEntity(sysMenuFormMap);
	return AttrConstants.SUCCESS;
    }

    /**
     * 更新菜单
     * 
     * @param model
     * @param Map
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    @Transactional(readOnly = false) /** 需要事务操作必须加入此注解 */
    @SystemLog(module = "系统管理", methods = "菜单管理-修改资源") /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    public String editEntity(Model model) throws Exception {
	SysMenuFormMap sysMenuFormMap = getFormMap(SysMenuFormMap.class);
	sysMenuMapper.editEntity(sysMenuFormMap);
	return AttrConstants.SUCCESS;
    }

    /**
     * 根据ID删除菜单
     * 
     * @param model
     * @param ids
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
    @SystemLog(module = "系统管理", methods = "菜单管理-删除资源") /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    public String deleteEntity(Model model) throws Exception {
	String[] ids = SessionUtil.getParaValues("ids");
	String[] ids1 = ids[0].split(",");
	for (String id : ids1) {
	    sysMenuMapper.deleteByAttribute("menuId",id,SysMenuFormMap.class);
	}
	return AttrConstants.SUCCESS;
    }

    @RequestMapping("sortUpdate")
    @ResponseBody
    @SystemLog(module = "系统管理", methods = "菜单管理-菜单排序") /** 凡需要处理业务逻辑的.都需要记录操作日志 */
    public String sortUpdate(Params params) throws Exception {
	List<String> ids = params.getId();
	List<String> es = params.getRowId();
	List<SysMenuFormMap> maps = new ArrayList<SysMenuFormMap>();
	for (int i = 0; i < ids.size(); i++) {
	    SysMenuFormMap map = new SysMenuFormMap();
	    map.put("menuId", ids.get(i));
	    map.put("menuLevel", es.get(i));
	    maps.add(map);
	}
	sysMenuMapper.updateSortOrder(maps);
	return AttrConstants.SUCCESS;
    }

    @ResponseBody
    @RequestMapping("findRoleMenu")
    public List<SysMenuFormMap> findUserRes() {
	SysMenuFormMap sysMenuFormMap = getFormMap(SysMenuFormMap.class);
	List<SysMenuFormMap> rs = sysMenuMapper.findRoleMenu(sysMenuFormMap);
	return rs;
    }

    /**
     * 验证菜单是否存在
     * 
     * @return
     */
    @RequestMapping("isExist")
    @ResponseBody
    public boolean isExist(String menuId, String name, String menuKey, String menuUrl) {
	SysMenuFormMap sysMenuFormMap = new SysMenuFormMap();
	sysMenuFormMap.put("menuId", menuId);
	sysMenuFormMap.put("name", name);
	sysMenuFormMap.put("menuKey", menuKey);
	sysMenuFormMap.put("menuUrl", menuUrl);
	List<SysMenuFormMap> r = sysMenuMapper.count(sysMenuFormMap);
	int count = Integer.parseInt(r.get(0).get("count").toString());
	if (count > 0) {
	    return false;
	} else {
	    return true;
	}
    }

    /**
     * 联动下拉框获取ID等级
     * 
     * @param parentId
     * @return
     */
    @ResponseBody
    @RequestMapping("getLevel")
    public int getLevel(String parentId) {
	SysMenuFormMap sysMenuFormMap = new SysMenuFormMap();
	sysMenuFormMap.put("menuId", parentId);
	List<SysMenuFormMap> r = sysMenuMapper.findByWhere(sysMenuFormMap);
	// SysMenuFormMap r = sysMenuMapper.findbyFrist("menuId", parentId,
	// SysMenuFormMap.class);
	int selfLevel = Integer.parseInt(r.get(0).getStr("type")) + 1;
	return selfLevel;
    }
}