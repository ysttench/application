package com.ysttench.application.auth.settings.web.controller.system;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.auth.settings.kernel.entity.SysCodeConfigFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.SysCodeConfigMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.CodeConfigComponent;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.auth.settings.web.rdto.system.SysCodeConfig;
import com.ysttench.application.auth.settings.web.rdto.util.Params;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.HttpServletUtil;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.StringUtil;

/**
 * 系统配置
 * 
 * @author lixl
 * 
 */
@Controller
@RequestMapping("/codeconfig/")
public class SysCodeConfigController extends BaseController {
    @Inject
    private SysCodeConfigMapper sysCodeConfigMapper;

    @Inject
    private CodeConfigComponent codeConfigComponent;

    /**
     * 打开系统参数列表画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.SYSTEM + ForwardConstants.CODE_CONFIG + ForwardConstants.LIST;
    }

    /**
     * 配置参数列表画面分页处理
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
        SysCodeConfigFormMap sysCodeConfigFormMap = getFormMap(SysCodeConfigFormMap.class);
        sysCodeConfigFormMap = toFormMap(sysCodeConfigFormMap, pageNow, pageSize,
                sysCodeConfigFormMap.getStr("orderby"));
        sysCodeConfigFormMap.put("column", StringUtil.prop2tablefield(column));
        sysCodeConfigFormMap.put("sort", sort);

        sysCodeConfigFormMap.put("where", " and DELETED_Flag = 0 ");
        String param = sysCodeConfigFormMap.getStr("searchName");
        if (!StringUtil.isEmpty(param)) {
            sysCodeConfigFormMap.put("where", " and (CONFIG_NAME like '%" + param
                    + "%' or CONFIG_CODE like '%" + param + "%' or DESCRIPTION like '%" + param + "%')");
        }
        String parentConfigCode = SessionUtil.getPara("configCode");
        List<SysCodeConfigFormMap> lis = null;
        if (!parentConfigCode.equals("C0001")) {
            SysCodeConfigFormMap paramFormMap = this.codeConfigComponent.getParentFormMap(parentConfigCode);
            sysCodeConfigFormMap.put("parentId", paramFormMap.getInt("id"));
            lis = sysCodeConfigMapper.findChildsByParent(sysCodeConfigFormMap);
        } else {
            sysCodeConfigFormMap.put("parentId", 0);
            lis = sysCodeConfigMapper.findByPage(sysCodeConfigFormMap);
        }
        pageView.setRecords(lis);
        return pageView;
    }

    /**
     * 获取参数信息
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findData")
    public List<SysCodeConfigFormMap> findData(String pageNow, String pageSize, String column, String sort)
            throws Exception {
        SysCodeConfigFormMap sysCodeConfigFormMap = getFormMap(SysCodeConfigFormMap.class);
        sysCodeConfigFormMap.put("detailFlag", "1");
        sysCodeConfigFormMap.put("deletedFlag", "0");
        sysCodeConfigFormMap.put("orderby", "order by CREATE_TIME desc");
        List<SysCodeConfigFormMap> lis = sysCodeConfigMapper.findByNames(sysCodeConfigFormMap);
        return lis;
    }

    /**
     * 打开配置新增画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("addUI")
    public String addUI(Model model, HttpServletRequest request) throws Exception {
        // 获取父类名称代码
        String configCode = SessionUtil.getPara("configCode");
        // 若名称代码是配置参数初始值C0001，则父类名置为无
        if (configCode.equals("C0001")) {
            model.addAttribute("parentId", 0);
        } else {
            SysCodeConfigFormMap sysCodeConfigFormMap = this.codeConfigComponent.getParentFormMap(configCode);
            model.addAttribute("parentId", sysCodeConfigFormMap.get("id"));
            model.addAttribute("parentName", sysCodeConfigFormMap.get("configName"));
        }
        return ForwardConstants.SYSTEM + ForwardConstants.CODE_CONFIG + ForwardConstants.ADD;
    }

    /**
     * 保存配置新增信息
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("addEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "参数配置管理", methods = "参数配置管理-新增参数配置")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String addEntity(HttpServletRequest request) throws Exception {
        SysCodeConfigFormMap sysCodeConfigFormMap = getFormMap(SysCodeConfigFormMap.class);
/*        // 若名称代码没填，则提示错误
        String configCode = sysCodeConfigFormMap.getStr("configCode");
        if ("".equals(configCode) && "0".equals(sysCodeConfigFormMap.getStr("parentId"))) {
            return AttrConstants.NAME_CODE_NULL;
        }*/
        // 判断父类名称，添加数据
        doSaveInfo(sysCodeConfigFormMap, Integer.parseInt(sysCodeConfigFormMap.getStr("parentId")));
        return AttrConstants.SUCCESS;
    }

    /**
     * 根据父类的名称选择查询不同的数据
     * 
     * @param sysCodeConfigFormMap
     * @param parentName
     * @param configCode
     */
    private void doSaveInfo(SysCodeConfigFormMap sysCodeConfigFormMap, int parentId) {
        sysCodeConfigFormMap.put("createTime", DatetimeUtil.getDate());
        sysCodeConfigFormMap.put("deletedFlag", "0");
        sysCodeConfigFormMap.put("parentId", parentId);
        try {
            sysCodeConfigMapper.addEntity(sysCodeConfigFormMap);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 删除参数配置信息
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "配置参数管理", methods = "配置参数管理-删除配置信息")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String deleteEntity() throws Exception {
        String[] ids = SessionUtil.getParaValues("ids")[0].split(",");
        for (String id : ids) {
            SysCodeConfigFormMap sysCodeConfigFormMap = new SysCodeConfigFormMap();
            sysCodeConfigFormMap.put("id", id);
            sysCodeConfigFormMap.put("deletedFlag", "1");
            sysCodeConfigMapper.editEntity(sysCodeConfigFormMap);
        }
        return AttrConstants.SUCCESS;
    }

    /**
     * 进入参数配置编辑画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
        String id = SessionUtil.getPara("id");
        SysCodeConfigFormMap sysCodeConfigFormMap = sysCodeConfigMapper.findbyFrist("id", id,
                SysCodeConfigFormMap.class);
        model.addAttribute("parameter", sysCodeConfigFormMap);
        Integer parentId = sysCodeConfigFormMap.getInt("parentId");
        model.addAttribute("parentId", parentId);
        if (parentId != 0) {
            model.addAttribute("parentName",
                    this.codeConfigComponent.getParentFormMap(sysCodeConfigFormMap.getInt("parentId"))
                            .getStr("configName"));
        }
        return ForwardConstants.SYSTEM + ForwardConstants.CODE_CONFIG + ForwardConstants.EDIT;
    }

    /**
     * 编辑配置保存
     * 
     * @param txtGroupsSelect
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "配置参数管理", methods = "配置参数管理-修改配置参数")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String editEntity(String txtGroupsSelect) throws Exception {
        SysCodeConfigFormMap sysCodeConfigFormMap = getFormMap(SysCodeConfigFormMap.class);
  /*      // 若名称代码没填，则提示错误
        if ("".equals(sysCodeConfigFormMap.getStr("configCode"))
                && "0".equals(sysCodeConfigFormMap.getStr("parentId"))) {
            return AttrConstants.NAME_CODE_NULL;
        }*/
        sysCodeConfigMapper.editEntity(sysCodeConfigFormMap);
        return AttrConstants.SUCCESS;
    }

    /**
     * 做好下拉框排序
     * 
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping("sortUpdate")
    @ResponseBody
    @SystemLog(module = "配置参数管理", methods = "配置参数管理-配置参数排序")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String sortUpdate(Params params) throws Exception {
        List<String> ids = params.getId();
        List<String> es = params.getRowId();
        List<SysCodeConfigFormMap> maps = new ArrayList<SysCodeConfigFormMap>();
        for (int i = 0; i < ids.size(); i++) {
            SysCodeConfigFormMap map = new SysCodeConfigFormMap();
            map.put("id", ids.get(i));
            map.put("displayOrder", es.get(i));
            maps.add(map);
        }
        sysCodeConfigMapper.updateSortOrder(maps);
        return AttrConstants.SUCCESS;
    }

    /**
     * 验证参数配置名是否存在
     * 
     * @param value
     * @return
     */
    @RequestMapping("isExistConfigName")
    @ResponseBody
    public boolean isExistConfigName(String id, int parentId, String configName) {
        SysCodeConfigFormMap sysCodeConfigFormMap = new SysCodeConfigFormMap();
        if (!StringUtil.isEmpty(id)) {
            sysCodeConfigFormMap.put("where", " and id <> " + id);
        }
        sysCodeConfigFormMap.put("parentId", parentId);
        sysCodeConfigFormMap.put("configName", configName);
        List<SysCodeConfigFormMap> listSysCodeConfigFormMap = sysCodeConfigMapper
                .findByNames(sysCodeConfigFormMap);
        if (listSysCodeConfigFormMap.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 验证参数值是否存在
     * 
     * @param value
     * @return
     */
    @RequestMapping("isExistConfigValue")
    @ResponseBody
    public boolean isExistConfigValue(String id, int parentId, String configValue) {
        SysCodeConfigFormMap sysCodeConfigFormMap = new SysCodeConfigFormMap();
        if (!StringUtil.isEmpty(id)) {
            sysCodeConfigFormMap.put("where", " and id <> " + id);
        }
        sysCodeConfigFormMap.put("parentId", parentId);
        sysCodeConfigFormMap.put("configValue", configValue);
        List<SysCodeConfigFormMap> listSysCodeConfigFormMap = sysCodeConfigMapper
                .findByNames(sysCodeConfigFormMap);
        if (listSysCodeConfigFormMap.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 验证参数配置编码是否存在
     * 
     * @param value
     * @return
     */
    @RequestMapping("isExistConfigCode")
    @ResponseBody
    public boolean isExistConfigCode(String id, String configCode) {
        SysCodeConfigFormMap sysCodeConfigFormMap = new SysCodeConfigFormMap();
        if (!StringUtil.isEmpty(id)) {
            sysCodeConfigFormMap.put("where", " and id <> " + id);
        }
        sysCodeConfigFormMap.put("parentId", 0);
        sysCodeConfigFormMap.put("configCode", configCode);
        List<SysCodeConfigFormMap> listSysCodeConfigFormMap = sysCodeConfigMapper
                .findByNames(sysCodeConfigFormMap);
        if (listSysCodeConfigFormMap.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 参数配置信息查询
     * 
     * @param configCode
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findCodeListByConfigCode")
    public List<SysCodeConfig> findCodeListByConfigCode(String configCode) throws Exception {
        if (StringUtil.isEmpty(configCode)) {
            configCode = SessionUtil.getPara("configCode");
        }
        return this.codeConfigComponent.getCodeListByConfigCode(configCode);
    }

    /**
     * 参数Json配置信息查询
     * 
     * @param configCode
     * @return
     * @throws Exception
     */
    @RequestMapping("findDiscriptionByConfigCode")
    public void findDiscriptionByConfigCode(HttpServletResponse response, String configCode) throws Exception {
        if (StringUtil.isEmpty(configCode)) {
            configCode = SessionUtil.getPara("configCode");
        }
        String responseString = this.codeConfigComponent.getDiscriptionByConfigCode(configCode);
        HttpServletUtil.writeString(response, responseString);
    }

    /**
     * 根据编码及值，获取参数配置名称
     * 
     * @param configCode
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findConfigName")
    public String findConfigName(String configCode, String configValue) throws Exception {
        if (StringUtil.isEmpty(configCode)) {
            configCode = SessionUtil.getPara("configCode");
        }
        if (StringUtil.isEmpty(configValue)) {
            configValue = SessionUtil.getPara("configValue");
        }
        return this.codeConfigComponent.getConfigName(configCode, configValue);
    }

}
