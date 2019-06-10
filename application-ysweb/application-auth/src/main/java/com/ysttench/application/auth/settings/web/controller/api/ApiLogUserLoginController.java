package com.ysttench.application.auth.settings.web.controller.api;

import java.util.List;

import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.auth.settings.kernel.entity.ApiLogUserLoginFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.ApiLogUserLoginMapper;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.util.StringUtil;


/**
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/apiuserlogin/")
public class ApiLogUserLoginController extends BaseController {
    @Inject
    private ApiLogUserLoginMapper userLoginMapper;

    /**
     * 跳转到列表页面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.API + ForwardConstants.APIUSERLOGIN + ForwardConstants.LIST;
    }

    /**
     * 获取应用用户登录信息
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
        ApiLogUserLoginFormMap logUserLoginFormMap = getFormMap(ApiLogUserLoginFormMap.class);
        logUserLoginFormMap.put("column", StringUtil.prop2tablefield(column));
        logUserLoginFormMap.put("sort", sort);
        logUserLoginFormMap = toFormMap(logUserLoginFormMap, pageNow, pageSize, logUserLoginFormMap.getStr("orderby"));
        pageView.setRecords(userLoginMapper.findByPage(logUserLoginFormMap));
        return pageView;
    }

    /**
     * 获取应用系统对应的用户人数
     * @return
     */
    @ResponseBody
    @RequestMapping("datalist")
    public  List<ApiLogUserLoginFormMap>  getDataList() {
        List<ApiLogUserLoginFormMap> list = userLoginMapper.getNum(new ApiLogUserLoginFormMap());
/*        ApiLogUserLoginFormMap apiLogUserLoginFormMap = new ApiLogUserLoginFormMap();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                apiLogUserLoginFormMap.put(list.get(0).getStr("apiName"), list.get(0).getStr("apiName"));
            }
        }*/
        return list;
    }

}