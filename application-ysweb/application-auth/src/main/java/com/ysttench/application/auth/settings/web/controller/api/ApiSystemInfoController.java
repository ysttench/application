package com.ysttench.application.auth.settings.web.controller.api;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.auth.settings.kernel.entity.ApiSystemInfoFormMap;
import com.ysttench.application.auth.settings.kernel.entity.ApiUserFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.ApiSystemInfoMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.StringUtil;

/**
 * 
 * @author
 */
@Controller
@RequestMapping("/apisysteminfo/")
public class ApiSystemInfoController extends BaseController {
    @Inject
    private ApiSystemInfoMapper apiSystemInfoMapper;

    /**
     * 跳转到列表页面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.API+ForwardConstants.SYSTEMINFO+ForwardConstants.LIST;
    }

    /**
     * 获取分页显示数据
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
        ApiSystemInfoFormMap apiSystemInfoFormMap = getFormMap(ApiSystemInfoFormMap.class);
        apiSystemInfoFormMap = toFormMap(apiSystemInfoFormMap, pageNow, pageSize,
                apiSystemInfoFormMap.getStr("orderby"));
        apiSystemInfoFormMap.put("column", StringUtil.prop2tablefield(column));
        apiSystemInfoFormMap.put("sort", sort);
        pageView.setRecords(apiSystemInfoMapper.findSystemInfoPage(apiSystemInfoFormMap));// 不调用默认分页,调用自已的mapper中findUserPage
        return pageView;
    }

    /**
     * 进入查看画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model) {
        String id = SessionUtil.getPara("id");
        if (StringUtil.isNotEmpty(id)) {
            ApiSystemInfoFormMap apiSystemInfoFormMap = new ApiSystemInfoFormMap();
            apiSystemInfoFormMap.put("id", id);
            List<ApiUserFormMap> list = apiSystemInfoMapper.findDetailSystem(apiSystemInfoFormMap);
            model.addAttribute("user", list.get(0));
        }
        return ForwardConstants.API+ForwardConstants.SYSTEMINFO+ForwardConstants.DETAIL;
    }
    
    @ResponseBody
    @RequestMapping("addEntity")
    @Transactional(readOnly = false)
    public String addEntity() throws Exception {
        ApiSystemInfoFormMap apiSystemInfoFormMap = getFormMap(ApiSystemInfoFormMap.class);
        try {
            apiSystemInfoMapper.addEntity(apiSystemInfoFormMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加应用系统异常");
            return AttrConstants.FAIL;
        }
        return AttrConstants.SUCCESS;
    }
    


    /**
     * 获取接入系统数量
     * @return
     */
    @ResponseBody
    @RequestMapping("xtNum")
    public int getcount(){
        return Integer.parseInt(this.apiSystemInfoMapper.getCount(new ApiSystemInfoFormMap()));
    }

    
    /**校验应用Key是否存在
     * @param key
     * @return
     */
    @ResponseBody
    @RequestMapping("checkKeyExit")
    public Boolean checkKeyExit(String key){
        ApiSystemInfoFormMap apiSystemInfoFormMap = new ApiSystemInfoFormMap();
        apiSystemInfoFormMap.put("where", "where DELETED_FLAG='0'  AND SYS_KEY='"+key+"'");
        List<ApiSystemInfoFormMap> apiSystemInfoList = apiSystemInfoMapper.findByWhere(apiSystemInfoFormMap);
        if(null!=apiSystemInfoList){
            if(apiSystemInfoList.size()>0){
                return Boolean.FALSE;//存在
            }else{
                return Boolean.TRUE;
            }
        }else{
            return Boolean.TRUE;//不存在
        }
    }

}