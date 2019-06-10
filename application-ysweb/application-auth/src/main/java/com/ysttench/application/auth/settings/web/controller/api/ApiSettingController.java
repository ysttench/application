package com.ysttench.application.auth.settings.web.controller.api;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.auth.settings.kernel.entity.ApiSystemSettingFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.ApiSystemSettingMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.StringUtil;

@Controller
@RequestMapping("/apiSetting/")
public class ApiSettingController extends BaseController {
    @Inject
    private ApiSystemSettingMapper apiSystemSettingMapper;

    /**
     * 打开系统参数列表画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.API+ForwardConstants.SETTING+ForwardConstants.LIST;
    }

    /**
     * 系统参数列表分页处理
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
        ApiSystemSettingFormMap apiSystemSettingFormMap = getFormMap(ApiSystemSettingFormMap.class);
        apiSystemSettingFormMap = toFormMap(apiSystemSettingFormMap, pageNow, pageSize,
                apiSystemSettingFormMap.getStr("orderby"));
        apiSystemSettingFormMap.put("column", StringUtil.prop2tablefield(column));
        apiSystemSettingFormMap.put("sort", sort);
        pageView.setRecords(apiSystemSettingMapper.findSettingByPage(apiSystemSettingFormMap));
        return pageView;
    }

    /**
     * 打开系统参数新增画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("addUI")
    public String addUI(Model model) throws Exception {
        return ForwardConstants.API+ForwardConstants.SETTING+ForwardConstants.ADD;
    }

    /**
     * 保存系统参数信息
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("addEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "应用管理", methods = "系统参数管理-新增系统参数")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String addEntity() throws Exception {
        ApiSystemSettingFormMap apiSystemSettingFormMap = getFormMap(ApiSystemSettingFormMap.class);
        try {
            apiSystemSettingFormMap.put("createTime", DatetimeUtil.fromDateH());
            apiSystemSettingMapper.addEntity(apiSystemSettingFormMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加系统参数异常");
            return AttrConstants.FAIL;
        }
        return AttrConstants.SUCCESS;
    }

    /**
     * 系统参数删除操作
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "应用管理", methods = "系统参数管理-删除系统参数")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String deleteEntity() {
        String[] ids = SessionUtil.getPara("ids").split(",");
        for (String id : ids) {
            List<ApiSystemSettingFormMap> list = apiSystemSettingMapper.findByAttribute("ID", id,
                    ApiSystemSettingFormMap.class);
            if (list != null && !"".equals(list)) {
                if (list.get(0).get("deletedFlag").equals("1")) {
                    return AttrConstants.FAIL;
                } else {
                    ApiSystemSettingFormMap apiSystemSettingFormMap = new ApiSystemSettingFormMap();
                    apiSystemSettingFormMap.put("id", id);
                    apiSystemSettingFormMap.put("deletedFlag", "1");
                    try {
                        apiSystemSettingMapper.editEntity(apiSystemSettingFormMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("删除系统参数异常");
                        return AttrConstants.FAIL;
                    }
                }
            }
        }
        return AttrConstants.SUCCESS;
    }

    /**
     * 打开系统参数编辑画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
        String id = SessionUtil.getPara("id");
        if (StringUtil.isNotEmpty(id)) {
            model.addAttribute("systemsettingInfo",
                    apiSystemSettingMapper.findbyFrist("ID", id, ApiSystemSettingFormMap.class));
        }
        return ForwardConstants.API+ForwardConstants.SETTING+ForwardConstants.EDIT;
    }

    /**
     * 系统参数编辑保存
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "应用管理", methods = "参数管理-修改系统参数")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String editEntity() throws Exception {
        ApiSystemSettingFormMap apiSystemSettingFormMap = getFormMap(ApiSystemSettingFormMap.class);
        try {
            apiSystemSettingMapper.editEntity(apiSystemSettingFormMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新系统参数异常");
            return AttrConstants.FAIL;
        }
        return AttrConstants.SUCCESS;
    }

    /**
     * 验证系统参数是否存在
     * 
     * @param roleName
     * @return
     */
    @RequestMapping("isExist")
    @ResponseBody
    public boolean isExist(String name) {
        String[] setting = name.split(",");
        if (setting.length > 1) {
            List<ApiSystemSettingFormMap> roleInfo = apiSystemSettingMapper.findByAttribute("ID", setting[1],
                    ApiSystemSettingFormMap.class);
            if (roleInfo.get(0).get("sysKey").equals(setting[0])) {
                return true;
            }
        }
        List<ApiSystemSettingFormMap> lists = apiSystemSettingMapper.findByAttribute("SYS_KEY", setting[0],
                ApiSystemSettingFormMap.class);
        if (lists != null && lists.size() > 0) {
            return false;
        }
        return true;
    }


}