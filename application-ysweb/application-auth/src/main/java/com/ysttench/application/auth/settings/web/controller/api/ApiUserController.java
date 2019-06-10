package com.ysttench.application.auth.settings.web.controller.api;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.ysttench.application.auth.settings.kernel.entity.ApiUserFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.ApiUserMapper;
import com.ysttench.application.auth.settings.web.controller.common.AttrConstants;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;
import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.common.util.encrypt.Encrypt;
import com.ysttench.application.common.util.excel.POIUtils;
import com.ysttench.application.common.util.file.FileUtil;
import com.thoughtworks.xstream.core.util.Base64Encoder;

/**
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/apiUser/")
public class ApiUserController extends BaseController {
    @Inject
    private ApiUserMapper apiUserMapper;

    // 密码加密
    private Encrypt encrypt = new Encrypt();

    /**
     * 打开用户列表画面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        // model.addAttribute("res", findByRes());
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
        List<ApiUserFormMap> findByPage = apiUserMapper.findUserPage(apiFormMap);
        try {
            for(ApiUserFormMap apiUser:findByPage){
                if(null!=apiUser.getBytes("avatar")){
                    Blob serialBlob = new SerialBlob(apiUser.getBytes("avatar"));
                    InputStream binaryStream = serialBlob.getBinaryStream();
                    byte[] input2byte = FileUtil.input2byte(binaryStream);
                    String imageString = getImageString(input2byte);
                    apiUser.put("avatar", imageString);
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        pageView.setRecords(apiUserMapper.findUserPage(apiFormMap));// 不调用默认分页,调用自已的mapper中findUserPage
        return pageView;
    }
    
    /**获取字节流的base64编码的字符串
     * @param data
     * @return
     * @throws Exception
     */
    private String getImageString(byte[] data)throws Exception{
        Base64Encoder base64Encoder = new Base64Encoder();
        return data!=null?base64Encoder.encode(data):"";
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
        String fileName = "用户列表";
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
        for (ApiUserFormMap apiUserFormMap : lis) {
            if(apiUserFormMap.getStr("enabledFlag").equals("0")){
                apiUserFormMap.put("enabledFlag", "可用");
            }else{
                apiUserFormMap.put("enabledFlag", "禁用");
            }
        }
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
    public String addEntity(@RequestParam(value="file",required= false)MultipartFile myfile) {
        try {
            
            ApiUserFormMap apiUserFormMap = getFormMapHasFile(ApiUserFormMap.class);// 获取前台输入信息
            /*apiUserFormMap.put("txtGroupsSelect", txtGroupsSelect);*/
            if (apiUserFormMap.getStr("password") == null || "".equals(apiUserFormMap.getStr("password"))) {
                apiUserFormMap.set("password", "123456");
            }
            apiUserFormMap.put("password", encrypt.encoder(apiUserFormMap.getStr("password")));
            apiUserFormMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            apiUserFormMap.put("updateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            apiUserFormMap.put("avatar", myfile.getBytes());
            apiUserMapper.insertApiUser(apiUserFormMap);// 新增后返回新增信息
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加账号异常");
            return AttrConstants.FAIL;
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
        for (String id : ids) {
            List<ApiUserFormMap> list = apiUserMapper.findByAttribute("USER_ID", id, ApiUserFormMap.class);
            if (list != null && !"".equals(list)) {
                if (list.get(0).get("deleteFlag").equals("1")) {
                    return AttrConstants.FAIL;
                } else {
                    ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
                    apiUserFormMap.put("userId", id);
                    apiUserFormMap.put("deleteFlag", "1");
                    apiUserFormMap.put("deleteTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    try {
                        apiUserMapper.editEntity(apiUserFormMap);// 删除相当于修改删除标志
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("删除账号异常");
                        return AttrConstants.FAIL;
                    }
                }
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
            ApiUserFormMap  apiUser= apiUserMapper.findDetailUser(apiUserFormMap).get(0);
            apiUser.put("password", encrypt.decoder(apiUser.getStr("password")));
            if(null!=apiUser.getBytes("avatar")){
                Blob serialBlob = new SerialBlob(apiUser.getBytes("avatar"));
                InputStream binaryStream = serialBlob.getBinaryStream();
                byte[] input2byte = FileUtil.input2byte(binaryStream);
                String imageString = getImageString(input2byte);
                apiUser.put("avatar", imageString);
            }
            model.addAttribute("user", apiUser);
        }
        return ForwardConstants.API+ForwardConstants.USER+ForwardConstants.EDIT;
    }

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
    public String editEntity(String txtGroupsSelect,@RequestParam(value="file",required= false)MultipartFile myfile)throws Exception{
        ApiUserFormMap userFormMap = getFormMapHasFile(ApiUserFormMap.class);
        /*userFormMap.put("txtGroupsSelect", txtGroupsSelect);*/
        userFormMap.put("updateTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        userFormMap.put("avatar", myfile.getBytes());
        try {
            apiUserMapper.editApiUSer(userFormMap);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新账号异常");
            return AttrConstants.FAIL;
        }// 更新
        return AttrConstants.SUCCESS;
    }

    /**
     * 验证唯一和非空
     * 
     * @param name
     * @return
     */
    @RequestMapping("isExist")
    @ResponseBody
    public boolean emailIsOnly(String userId,String email,String phone,String sfzh,String userName,String policeId){
        ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
        apiUserFormMap.put("userId", userId);
        apiUserFormMap.put("email", email);
        apiUserFormMap.put("phone", phone);
        apiUserFormMap.put("sfzh", sfzh);
        apiUserFormMap.put("userName", userName);
        apiUserFormMap.put("policeId", policeId);
        List<ApiUserFormMap> user = apiUserMapper.count(apiUserFormMap);
        int count = Integer.parseInt(user.get(0).get("count").toString());
        if (count > 0) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * 获取所有用户的部门
     * 
     * @return
     */
    @RequestMapping("findBM")
    @ResponseBody
    public List<ApiUserFormMap> findBM() {
        return apiUserMapper.findBM(new ApiUserFormMap());
    }
    /**
     * 获取用户数量
     * @return
     */
    @ResponseBody
    @RequestMapping("yhNum")
    public int getcount(){
        return Integer.parseInt(this.apiUserMapper.getCount(new ApiUserFormMap()));
    }
}