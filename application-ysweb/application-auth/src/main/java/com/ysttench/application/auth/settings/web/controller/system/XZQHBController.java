package com.ysttench.application.auth.settings.web.controller.system;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.auth.settings.kernel.entity.XZQHBFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.XZQHBMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.CodeConfigComponent;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.StringUtil;

/**
 * @author Howard
 * 行政区划
 */
@Controller
@RequestMapping("/xzqhb/")
public class XZQHBController extends BaseController {
    @Inject
    private XZQHBMapper xzqhbMapper;
    @Inject
    private CodeConfigComponent codeConfigComponent;

    /**
     * 进入行政区划管理页面
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.SYSTEM + ForwardConstants.XZQHB + ForwardConstants.LIST;
    }

    /**
     * 导入行政区划记录
     * @param pageNow
     * @param pageSize
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("findByPage")
    public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
        XZQHBFormMap xzqhbFormMap = getFormMap(XZQHBFormMap.class);
        xzqhbFormMap.put("column", StringUtil.prop2tablefield(column));
        xzqhbFormMap.put("sort", sort);
        if (xzqhbFormMap.containsKey("searchValue")) {
            String searchValue = xzqhbFormMap.get("searchValue").toString();
            xzqhbFormMap.set("where", " and (mc like '%" + searchValue + "%' or dm like '%" + searchValue + "%' or zwpy like '%" + searchValue + "%' or bz like '%" + searchValue + "%')");
        }
        xzqhbFormMap = toFormMap(xzqhbFormMap, pageNow, pageSize, xzqhbFormMap.getStr("orderby"));
        pageView.setRecords(xzqhbMapper.findByPage(xzqhbFormMap));
        return pageView;
    }

    /**
     * 进入添加行政区划页面
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("addUI")
    public String addUI(Model model) throws Exception {
        return ForwardConstants.SYSTEM + ForwardConstants.XZQHB + ForwardConstants.ADD;
    }

    /**
     * 添加行政区划
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("addEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "系统管理", methods = "行政区划管理-新增行政区划")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String addEntity() throws Exception {
        XZQHBFormMap xzqhbFormMap = getFormMap(XZQHBFormMap.class);
        xzqhbFormMap .put("bdsj", DatetimeUtil.getDateyyyyMMddhhmmss());
//        xzqhbFormMap .put("bdlx", "I");
        xzqhbFormMap .put("qybz", "0");
        xzqhbFormMap .put("dzbm", UUID.randomUUID());
        xzqhbMapper.addEntity(xzqhbFormMap);
        return AttrConstants.SUCCESS;
    }

    /**
     * 删除行政区划
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "系统管理", methods = "行政区划管理-删除行政区划")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String deleteEntity() throws Exception {
        String[] ids = SessionUtil.getParaValues("ids");
        for (String id : ids) {
        	XZQHBFormMap xZQHBFormMap = new XZQHBFormMap();
        	xZQHBFormMap.put("dzbm", id);
            xzqhbMapper.deleteByAttribute(xZQHBFormMap);
        }
        return AttrConstants.SUCCESS;
    }

    /**
     * 进入编辑行政区划页面
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
        String dzbm = SessionUtil.getPara("id");
        if (StringUtil.isNotEmpty(dzbm)) {
        	XZQHBFormMap xzqhbFormMap = new XZQHBFormMap();
        	xzqhbFormMap.put("dzbm", dzbm);
            model.addAttribute("xzqhb", xzqhbMapper.findbyFrist(xzqhbFormMap));
        }
        return ForwardConstants.SYSTEM + ForwardConstants.XZQHB + ForwardConstants.EDIT;
    }

    /**
     * 编辑行政区划保存
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "系统管理", methods = "行政区划管理-修改行政区划")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String editEntity() throws Exception {
        XZQHBFormMap xzqhbFormMap = getFormMap(XZQHBFormMap.class);
        xzqhbFormMap .put("bdsj", DatetimeUtil.getDateyyyyMMddhhmmss());
        //xzqhbFormMap .put("qybz", "0");
        xzqhbMapper.editEntity(xzqhbFormMap);
        return AttrConstants.SUCCESS;
    }

    /**
     * 验证行政区划代码是否存在
     * @param value
     * @return
     */
    @RequestMapping("isExistDM")
    @ResponseBody
    public boolean isExistDM(String dm) {
    	XZQHBFormMap xzqhbFormMap = new XZQHBFormMap();
    	xzqhbFormMap.put("dm", dm);
        XZQHBFormMap account = xzqhbMapper.findbyFrist(xzqhbFormMap);
        if (account == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证行政区划代码是否存在
     * @param value
     * @return
     */
    @RequestMapping("isExistDMForEdit")
    @ResponseBody
    public boolean isExistDMForEdit(String dm, String dzbm) {
        XZQHBFormMap paramFormMap = new XZQHBFormMap();
        paramFormMap.put("where", " where DM = '" + dm + "' and DZBM <> '" + dzbm + "'  ");
        List<XZQHBFormMap> listXZQHBFormMap = xzqhbMapper.findByWhere(paramFormMap);
        if (listXZQHBFormMap.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证行政区划是否存在
     * @param value
     * @return
     */
    @RequestMapping("isExist")
    @ResponseBody
    public boolean isExist(String name) {
    	XZQHBFormMap xzqhbFormMap = new XZQHBFormMap();
    	xzqhbFormMap.put("mc", name);
        XZQHBFormMap account = xzqhbMapper.findbyFrist(xzqhbFormMap);
        if (account == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证行政区划是否存在
     * @param value
     * @return
     */
    @RequestMapping("isExistForEdit")
    @ResponseBody
    public boolean isExistForEdit(String name, String dzbm) {
        XZQHBFormMap paramFormMap = new XZQHBFormMap();
        paramFormMap.put("where", " where MC = '" + name + "' and DZBM <> '" + dzbm + "'  ");
        List<XZQHBFormMap> listXZQHBFormMap = xzqhbMapper.findByWhere(paramFormMap);
        if (listXZQHBFormMap.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取国家信息
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("getCountry")
    public List<XZQHBFormMap> getCountry() throws Exception {
        XZQHBFormMap xzqhbFormMap = new XZQHBFormMap();
        xzqhbFormMap.put("dm", "01%");
        List<XZQHBFormMap> listXZQHBFormMap = xzqhbMapper.findByNames(xzqhbFormMap);
        return listXZQHBFormMap;
    }

    /**
     * 获取国家信息
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("getProvince")
    public List<XZQHBFormMap> getProvince() throws Exception {
        XZQHBFormMap xzqhbFormMap = new XZQHBFormMap();
        xzqhbFormMap.put("dm", "%0000");
        xzqhbFormMap.put("qybz", "1");
        List<XZQHBFormMap> listXZQHBFormMap = xzqhbMapper.findByNames(xzqhbFormMap);
        return listXZQHBFormMap;
    }

    /**
     * 获取市列表信息
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("getCity")
    public List<XZQHBFormMap> getCity(String provinceId) throws Exception {
        if (StringUtil.isEmpty(provinceId) || provinceId.length() != 6) return null;
        String param = provinceId.substring(0, 2);
        List<XZQHBFormMap> listXZQHBFormMap = xzqhbMapper.findCityByProvince(param);
        return listXZQHBFormMap;
    }

    /**
     * 获取区县列表信息
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("getDistrict")
    public List<XZQHBFormMap> getDistrict(String cityId) throws Exception {
        if (StringUtil.isEmpty(cityId) || cityId.length() != 6) return null;
        String param = cityId.substring(0, 4);
        List<XZQHBFormMap> listXZQHBFormMap = xzqhbMapper.findDistrictByCity(param);
        return listXZQHBFormMap;
    }

    /**
     * 通过编码获取获取行政区划名称信息
     * @param model
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("getXzqhName")
    public String getXzqhName(String code) throws Exception {
        if (StringUtil.isEmpty(code)) code = SessionUtil.getPara("code");
        if (StringUtil.isEmpty(code) || code.length() != 6) return null;
        XZQHBFormMap xzqhbFormMap = new XZQHBFormMap();
        xzqhbFormMap.put("dm", code);
        XZQHBFormMap xZQHBFormMap = xzqhbMapper.findbyFrist(xzqhbFormMap);
        return (xZQHBFormMap==null)?"":xZQHBFormMap.getStr("mc");
    }
    
    /**获取行政区划全部数据
     * @return
     */
    @ResponseBody
    @RequestMapping("getAllData")
    public List<XZQHBFormMap> getAllData(){
        XZQHBFormMap xZQHBFormMap=new XZQHBFormMap();
        List<XZQHBFormMap> xZQHBFormMapList = xzqhbMapper.findByWhere(xZQHBFormMap);
        return xZQHBFormMapList;
    }
    

    /**
     * 获取行政区划编码
     */
    @ResponseBody
    @RequestMapping("getXzqh")
    public String getXzqh(){
        return this.codeConfigComponent.getConfigValueByConfigCode(AttrConstants.XZHQ_CODE);
    }

}