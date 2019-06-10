package com.ysttench.application.auth.settings.web.controller.system;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.auth.settings.kernel.entity.SysCodeConfigFormMap;
import com.ysttench.application.auth.settings.kernel.entity.SysDepartmentFormMap;
import com.ysttench.application.auth.settings.kernel.entity.XZQHBFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.SysCodeConfigMapper;
import com.ysttench.application.auth.settings.kernel.mapper.SysDepartmentMapper;
import com.ysttench.application.auth.settings.kernel.mapper.XZQHBMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.common.TreeUtilOfDepartment;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.auth.settings.web.rdto.util.DepartmentTreeObject;
import com.ysttench.application.auth.settings.web.rdto.util.MenuTreeObjectUtil;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.common.util.StringUtil;

/**
 * @author 潘益孟 类说明：部门控制类
 */
@Controller
@RequestMapping("/sysdepartment/")
public class SysDepartmentController extends BaseController implements Runnable {

    @Inject
    private XZQHBMapper xzqhbMapper;
    @Autowired
    private SysDepartmentMapper sysDepartmentMapper;
    @Autowired
    private SysCodeConfigMapper sysCodeConfigMapper;

    public SysDepartmentController(SysDepartmentMapper sysDepartmentMapper, SysCodeConfigMapper sysCodeConfigMapper) {
        this.sysDepartmentMapper = sysDepartmentMapper;
        this.sysCodeConfigMapper = sysCodeConfigMapper;
    }

    /**
     * 进入部门管理页面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.SYSTEM + ForwardConstants.DEPARTMENT + ForwardConstants.LIST;
    }

    /**
     * 导入部门记录
     * 
     * @param pageNow
     * @param pageSize
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findByPage")
    public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
        SysDepartmentFormMap sysDepartmentFormMap = getFormMap(SysDepartmentFormMap.class);
        sysDepartmentFormMap = toFormMap(sysDepartmentFormMap, pageNow, pageSize,
                sysDepartmentFormMap.getStr("orderby"));
        sysDepartmentFormMap.put("column", StringUtil.prop2tablefield(column));
        sysDepartmentFormMap.put("sort", sort);
        pageView.setRecords(sysDepartmentMapper.findDepartmentByPage(sysDepartmentFormMap));
        return pageView;
    }

    /**
     * 进入添加部门页面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("addUI")
    public String addUI(Model model) throws Exception {
        model.addAttribute("parent", sysDepartmentMapper.findParent(new SysDepartmentFormMap()));

        return ForwardConstants.SYSTEM + ForwardConstants.DEPARTMENT + ForwardConstants.ADD;
    }

    /**
     * 添加部门
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("addEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "系统管理", methods = "部门管理-新增部门")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String addEntity() throws Exception {
        SysDepartmentFormMap sysDepartmentFormMap = getFormMap(SysDepartmentFormMap.class);
        //sysDepartmentFormMap.put("id", UUID.randomUUID().toString());
        sysDepartmentFormMap.put("status", "0");
        if (sysDepartmentFormMap.get("parentId") == null || "".equals(sysDepartmentFormMap.get("parentId"))) {
            sysDepartmentFormMap.put("parentId", 0);
        }
        sysDepartmentMapper.addEntity(sysDepartmentFormMap);
        return AttrConstants.SUCCESS;
    }

    /**
     * 删除部门
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "系统管理", methods = "部门管理-删除部门")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String deleteEntity() throws Exception {
        String[] ids = SessionUtil.getParaValues("ids");
        for (String id : ids) {
            SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
            sysDepartmentFormMap.put("id", id);
            sysDepartmentFormMap.put("status", "1");
            sysDepartmentMapper.editEntity(sysDepartmentFormMap);
        }
        return AttrConstants.SUCCESS;
    }

    /**
     * 进入编辑部门页面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
        String id = SessionUtil.getPara("id");
        if (StringUtil.isNotEmpty(id)) {
            SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
            sysDepartmentFormMap.put("id", id);
            SysDepartmentFormMap sysDepartment = sysDepartmentMapper.findDetailDepartment(sysDepartmentFormMap);
            SysDepartmentFormMap parent = sysDepartmentMapper.findbyFrist("id", sysDepartment.getStr("parentId"),
                    SysDepartmentFormMap.class);
            XZQHBFormMap xzqhbFormMap = new XZQHBFormMap();
            xzqhbFormMap.put("dm", sysDepartment.getStr("xzqh"));
            XZQHBFormMap xzqh = xzqhbMapper.findbyFrist(xzqhbFormMap);
            sysDepartment.put("parentDeptName", parent == null ? "" : parent.getStr("deptName"));
            sysDepartment.put("xzqh", xzqh== null ?"":xzqh.getStr("dm"));
            sysDepartment.put("xaqhmc", xzqh== null ?"":xzqh.getStr("mc"));
            List<XZQHBFormMap> list = getAllData();
            model.addAttribute("sysDepartment", sysDepartment);
            model.addAttribute("xzqh",list);
            return ForwardConstants.SYSTEM + ForwardConstants.DEPARTMENT + ForwardConstants.EDIT;
        }
        return null;
    }

    /**
     * 部门选择画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("deptUI")
    public String deptUI(Model model) throws Exception {
        return ForwardConstants.SYSTEM + ForwardConstants.DEPARTMENT + ForwardConstants.DEPART;
    }

    @ResponseBody
    @RequestMapping("getParentIds")
    public String getParentIds(String parentId) {
        List<String> list = new ArrayList<>();
        list.add(parentId);
        getParent(parentId, list);
        String json = "[";
        for (int i = list.size() - 1; i >= 0; i--) {
            json += "\"" + list.get(i) + "\"";
            if (i != 0) {
                json += ",";
            }
        }
        json += "]";
        return json;
    }
    /**获取行政区划全部数据
     * @return
     */
    public List<XZQHBFormMap> getAllData(){
        XZQHBFormMap xZQHBFormMap=new XZQHBFormMap();
        List<XZQHBFormMap> xZQHBFormMapList = xzqhbMapper.findByWhere(xZQHBFormMap);
        return xZQHBFormMapList;
    }
    /**
     * 获取部门及上级部门名称
     * 
     * @param deptId
     * @return
     */
    @ResponseBody
    @RequestMapping("getParentDeptName")
    public String getParentDeptName(String deptId) {
        SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
        sysDepartmentFormMap.put("id", deptId);
        SysDepartmentFormMap sysDepartment = sysDepartmentMapper.findDetailDepartment(sysDepartmentFormMap);
        return sysDepartment == null ? "" : sysDepartment.getStr("parentDeptName");
    }

    public void getParent(String parentId, List<String> list) {
        SysDepartmentFormMap sysDepartment = sysDepartmentMapper.findbyFrist("id", parentId,
                SysDepartmentFormMap.class);
        if (sysDepartment != null) {
            if (StringUtil.isNotEmpty(sysDepartment.getStr("parentId"))
                    && (!sysDepartment.getStr("parentId").endsWith("0"))) {
                list.add(sysDepartment.getStr("parentId"));
                getParent(sysDepartment.getStr("parentId"), list);
            }
        }

    }

    /**
     * 编辑部门保存
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "系统管理", methods = "部门管理-修改部门")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String editEntity() throws Exception {
        SysDepartmentFormMap sysDepartmentFormMap = getFormMap(SysDepartmentFormMap.class);
        sysDepartmentMapper.editEntity(sysDepartmentFormMap);
        return AttrConstants.SUCCESS;
    }

    /**
     * 验证部门编码是否存在(新增用)
     * 
     * @param value
     * @return
     */
    @RequestMapping("isExistDeptCode")
    @ResponseBody
    public boolean isExistDeptCode(String deptCode) {
        SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
        sysDepartmentFormMap.put("deptCode", deptCode);
        List<SysDepartmentFormMap> listSysDepartmentFormMap = sysDepartmentMapper.findByNames(sysDepartmentFormMap);
        if (listSysDepartmentFormMap.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证部门名称是否存在(新增用)
     * 
     * @param value
     * @return
     */
    @RequestMapping("isExistDeptName")
    @ResponseBody
    public boolean isExistDeptName(String deptName) {
        SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
        sysDepartmentFormMap.put("deptName", deptName);
        List<SysDepartmentFormMap> listSysDepartmentFormMap = sysDepartmentMapper.findByNames(sysDepartmentFormMap);
        if (listSysDepartmentFormMap.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证部门名称是否存在(编辑用)
     * 
     * @param value
     * @return
     */
    @RequestMapping("isExistNameForEdit")
    @ResponseBody
    public boolean isExistCaseNameForEdit(String id, String name) {
        SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
        sysDepartmentFormMap.put("where", "and NAME ='" + name + "' and ID !='" + id + "'");
        List<SysDepartmentFormMap> listSysDepartmentFormMap = sysDepartmentMapper.findByNames(sysDepartmentFormMap);
        if (listSysDepartmentFormMap.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取部门
     * 
     * @return
     */
    @RequestMapping("findDeptName")
    @ResponseBody
    public List<SysDepartmentFormMap> findDeptName() {
        SysDepartmentFormMap deptFormMap = new SysDepartmentFormMap();
        deptFormMap.put("where", "where STATUS = 0");
        List<SysDepartmentFormMap> deptList = sysDepartmentMapper.findByWhere(deptFormMap);
        return deptList;

    }

    /**
     * 获取部门tree型数据
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("getAllDepartmentTreeData")
    public String getAllDepartmentTreeData() {
        SysCodeConfigFormMap sysCodeConfig = this.sysCodeConfigMapper.findbyFrist("configCode", "C0018",
                SysCodeConfigFormMap.class);
        if (null != sysCodeConfig) {
            String str = sysCodeConfig.getStr("description");
            return str;
        }
        return null;
    }

    @Override
    public void run() {
        SysDepartmentFormMap sysDepartmentFormMap = new SysDepartmentFormMap();
        sysDepartmentFormMap.put("status", "0");
        List<SysDepartmentFormMap> findByNames = sysDepartmentMapper.findAllData(sysDepartmentFormMap);

        List<DepartmentTreeObject> list = new ArrayList<DepartmentTreeObject>();
        for (SysDepartmentFormMap map : findByNames) {
            DepartmentTreeObject ts = new DepartmentTreeObject();
            MenuTreeObjectUtil.map2Tree(ts, map, "id");
            list.add(ts);
        }
        TreeUtilOfDepartment treeUtil = new TreeUtilOfDepartment();
        List<DepartmentTreeObject> ns = treeUtil.getChildTreeObjects(list, "0");

        String list2json = JsonUtils.list2json(ns);

        SysCodeConfigFormMap sysCodeConfigFormMap = new SysCodeConfigFormMap();
        sysCodeConfigFormMap.put("configCode", "C0018");
        SysCodeConfigFormMap sysCodeConfig = sysCodeConfigMapper.findByNameCode(sysCodeConfigFormMap);
        try {
            if (null != sysCodeConfig) {
                int id = sysCodeConfig.getInt("id");
                SysCodeConfigFormMap sysCodeConfigJson = new SysCodeConfigFormMap();
                sysCodeConfigJson.put("id", id);
                sysCodeConfigJson.put("description", list2json);
                this.sysCodeConfigMapper.editEntity(sysCodeConfigJson);
            } else {
                SysCodeConfigFormMap sysCodeConfigJson = new SysCodeConfigFormMap();
                sysCodeConfigJson.put("configCode", "C0018");
                sysCodeConfigJson.put("deleted", "0");
                sysCodeConfigJson.put("description", list2json);
                this.sysCodeConfigMapper.addEntity(sysCodeConfigJson);
            }
        } catch (Exception e) {
        }

    }

    /**
     * 获取部门数量
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("bmNum")
    public int getcount() {
        return Integer.parseInt(this.sysDepartmentMapper.getCount(new SysDepartmentFormMap()));
    }
}