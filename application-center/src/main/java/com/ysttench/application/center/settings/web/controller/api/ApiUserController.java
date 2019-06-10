package com.ysttench.application.center.settings.web.controller.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.center.settings.kernel.entity.ApiUserFormMap;
import com.ysttench.application.center.settings.kernel.mapper.ApiUserMapper;
import com.ysttench.application.center.settings.web.controller.common.AttrConstants;
import com.ysttench.application.center.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.center.settings.web.controller.index.BaseController;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.exception.SystemException;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.common.util.excel.POIUtils;

/**
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/apiuser/")
public class ApiUserController extends BaseController {
    @Inject
    private ApiUserMapper apiUserMapper;


    /**
     * 打开用户列表画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.API+ForwardConstants.USER+ForwardConstants.LIST;
    }

    /**
     * 用户列表画面分页处理
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
        ApiUserFormMap apiFormMap = getFormMap(ApiUserFormMap.class);
        apiFormMap = toFormMap(apiFormMap, pageNow, pageSize, apiFormMap.getStr("orderby"));
        apiFormMap.put("column", StringUtil.prop2tablefield(column));
        apiFormMap.put("sort", sort);
        pageView.setRecords(apiUserMapper.findUserPage(apiFormMap));// 不调用默认分页,调用自已的mapper中findUserPage
        return pageView;
    }

    /**
     * 导出用户列表
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/export")
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "客户公司列表";
        ApiUserFormMap userFormMap = findHasHMap(ApiUserFormMap.class);
        // exportData =
        // [{"colkey":"sql_info","name":"SQL语句","hide":false},
        // {"colkey":"total_time","name":"总响应时长","hide":false},
        // {"colkey":"avg_time","name":"平均响应时长","hide":false},
        // {"colkey":"record_time","name":"记录时间","hide":false},
        // {"colkey":"call_count","name":"请求次数","hide":false}
        // ]
        String exportData = userFormMap.getStr("exportData");// 列表头的json字符串
        String column = userFormMap.getStr("column");
        userFormMap.put("column", StringUtil.prop2tablefield(column));
        List<Map<String, Object>> listMap = JsonUtils.parseJSONList(exportData);
        List<ApiUserFormMap> lis = apiUserMapper.findUserPage(userFormMap);
        POIUtils.exportToExcel(response, listMap, lis, fileName);
    }

    /**
     * 新增用户
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("addUI")
    public String addUI(Model model) throws Exception {
        return ForwardConstants.API+ForwardConstants.USER+ForwardConstants.ADD;
    }

    /**
     * 添加用户
     * 
     * @param txtGroupsSelect
     * @return
     */
    @ResponseBody
    @RequestMapping("addEntity")
    @SystemLog(module = "应用管理", methods = "用户管理-新增用户") // 凡需要处理业务逻辑的.都需要记录操作日志
    @Transactional(readOnly = false) // 需要事务操作必须加入此注解
    public String addEntity() {
    	try {
    		ApiUserFormMap apiUserFormMap = getFormMap(ApiUserFormMap.class);
    		apiUserMapper.addEntity(apiUserFormMap);
		} catch (Exception e) {
			throw new SystemException("添加异常");
		}
		return AttrConstants.SUCCESS;
		}

    /**
     * 删除用户
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "应用管理", methods = "用户管理-删除用户")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String deleteEntity() {
        String[] ids = SessionUtil.getParaValues("ids");
        String[] ids1 = ids[0].split(",");
        for (String id : ids1) {
            ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
            apiUserFormMap.put("id", id);
            apiUserFormMap.put("deleteStatus", "1");
            try {
		apiUserMapper.editEntity(apiUserFormMap);
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
        }
        return AttrConstants.SUCCESS;
        }

    /**
     * 进入用户编辑画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model)throws Exception  {
    	String id = SessionUtil.getPara("id");
        if (StringUtil.isNotEmpty(id)) {
            ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
            apiUserFormMap.put("id", id);
            List<ApiUserFormMap> list = apiUserMapper.findDetailUser(apiUserFormMap);
            model.addAttribute("user", list.get(0));
        }
        return ForwardConstants.API + ForwardConstants.USER + ForwardConstants.EDIT;}

    /**
     * 编辑用户保存
     * 
     * @param txtGroupsSelect
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "应用管理", methods = "用户管理-修改用户")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String editEntity()throws Exception{
    	try {
    		ApiUserFormMap apiFormMap = getFormMap(ApiUserFormMap.class);
    		apiUserMapper.editEntity(apiFormMap);
		} catch (Exception e) {
			throw new SystemException("编辑异常");
		}
        return AttrConstants.SUCCESS;}

    /**
     * 验证唯一和非空
     * 
     * @param name
     * @return
     */
    @RequestMapping("isExist")
    @ResponseBody
    public boolean isExist(String id,String companyName){
    	ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
    	apiUserFormMap.put("id", id);
    	apiUserFormMap.put("companyName", companyName);
        List<ApiUserFormMap> user = apiUserMapper.count(apiUserFormMap);
        int count = Integer.parseInt(user.get(0).get("count").toString());
        if (count > 0) {
            return false;
        } else {
            return true;
        }
    }
    
  
}