package com.ysttench.application.htbw.settings.web.controller.system;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.exception.SystemException;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.common.util.encrypt.Encrypt;
import com.ysttench.application.common.util.excel.ExcelUtil;
import com.ysttench.application.htbw.settings.kernel.entity.SysUserFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.SysUserRoleFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.SysUserMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.SysUserRoleMapper;
import com.ysttench.application.htbw.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htbw.settings.web.controller.index.BaseController;

/**
 * 
 * @author Howard
 */
@Controller
@RequestMapping("/user/")
public class SysUserController extends BaseController {
    @Inject
    private SysUserMapper sysUserMapper;
    @Inject
    private SysUserRoleMapper sysUserRoleMapper;
    /** 密码加密*/
    private Encrypt encrypt = new Encrypt();

    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
        return ForwardConstants.SYSTEM + ForwardConstants.USER + ForwardConstants.LIST;
    }

    @ResponseBody
    @RequestMapping("findByPage")
    public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
        SysUserFormMap userFormMap = getFormMap(SysUserFormMap.class);
        userFormMap = toFormMap(userFormMap, pageNow, pageSize, userFormMap.getStr("orderby"));
        userFormMap.put("column", StringUtil.prop2tablefield(column));
        userFormMap.put("sort", sort);
        if (!"admin".equals(((SysUserFormMap) SessionUtil.getUserSession()).getStr("userName"))) {
            userFormMap.put("where", " and USER_NAME <> 'admin'");
        }
        // 不调用默认分页,调用自已的mapper中findUserPage
        pageView.setRecords(sysUserMapper.findUserPage(userFormMap));
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
    public void download(HttpServletRequest request, HttpServletResponse response, String column,String sort) throws IOException {
        String fileName = "用户列表";
        SysUserFormMap userFormMap = findHasHMap(SysUserFormMap.class);
        userFormMap.put("column", StringUtil.prop2tablefield(column));
        userFormMap.put("sort", sort);
        // 列表头的json字符串
        String exportData = userFormMap.getStr("exportData");

        List<Map<String, Object>> listMap = JsonUtils.parseJSONList(exportData);

        List<SysUserFormMap> lis = sysUserMapper.findUserPage(userFormMap);
        for (SysUserFormMap sysUserFormMap : lis) {
            if(sysUserFormMap.get("locked").toString().equals("0")){
                sysUserFormMap.put("locked", "可用");
            }else{
                sysUserFormMap.put("locked", "禁用");
            }
        }
        this.myexportToExcel(response, listMap, lis, fileName);
    }

    /**
     * @author Howard
     * @param exportData
     *            列表头
     * @param lis
     *            数据集
     * @param fileName
     *            文件名
     * 
     */
    public void myexportToExcel(HttpServletResponse response, List<Map<String, Object>> exportData, List<?> lis,
            String fileName) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ExcelUtil.createWorkBook(exportData, lis).write(os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((fileName + ".xls").getBytes("UTF-8"), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();}
                if (bos != null) {
                    bos.close();}
            } catch (IOException e) {
            }

        }
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
        return ForwardConstants.SYSTEM + ForwardConstants.USER + ForwardConstants.ADD;
    }

    /**
     * 添加用户
     * 
     * @param txtGroupsSelect
     * @return
     */
    @ResponseBody
    @RequestMapping("addEntity")
    @SystemLog(module = "系统管理", methods = "用户管理-新增用户") /** 凡需要处理业务逻辑的.都需要记录操作日志*/
    @Transactional(readOnly = false) /** 需要事务操作必须加入此注解*/
    public String addEntity(String txtGroupsSelect) {
        try {
            if (txtGroupsSelect == null || txtGroupsSelect.equals("")) {
                return AttrConstants.FAIL;
            } else {
                SysUserFormMap sysUserFormMap = getFormMap(SysUserFormMap.class);
                sysUserFormMap.put("txtGroupsSelect", txtGroupsSelect);
                if (sysUserFormMap.getStr("password") != null || !"".equals(sysUserFormMap.getStr("password"))) {
                    sysUserFormMap.set("password", "123456");
                }
                sysUserFormMap.put("password", encrypt.encoder(sysUserFormMap.getStr("password")));
                // 新增后返回新增信息
                sysUserFormMap.put("createTime", DatetimeUtil.getDate());
                sysUserMapper.addEntity(sysUserFormMap);
                if (!StringUtil.isEmpty(txtGroupsSelect)) {
                    String[] txt = txtGroupsSelect.split(",");
                    SysUserRoleFormMap userGroupsFormMap = new SysUserRoleFormMap();
                    for (String roleId : txt) {
                        userGroupsFormMap.put("userId", sysUserFormMap.get("id"));
                        userGroupsFormMap.put("roleId", roleId);
                        sysUserRoleMapper.addEntity(userGroupsFormMap);
                    }
                }
            }
        } catch (Exception e) {
            throw new SystemException("添加账号异常");
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
    /** 需要事务操作必须加入此注解 */
    @Transactional(readOnly = false)
    /**  凡需要处理业务逻辑的.都需要记录操作日志 */
    @SystemLog(module = "系统管理", methods = "用户管理-删除用户")
    public String deleteEntity()  {
	try {
	    String[] ids = SessionUtil.getParaValues("ids");
	    String[] ids1 = ids[0].split(",");
	    for (String id : ids1) {
		if (id.equals(SessionUtil.getUserIdSession())) {
		    return AttrConstants.FAIL;
		}
		SysUserFormMap sysUserFormMap = new SysUserFormMap();
		sysUserFormMap.put("userId", id);
		sysUserFormMap.put("deleteStatus", "1");
		sysUserMapper.editEntity(sysUserFormMap);
		sysUserRoleMapper.deleteByAttribute("userId", id, SysUserRoleFormMap.class);
	    }
	} catch (Exception e) {
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
    public String editUI(Model model) throws Exception {
        String id = SessionUtil.getPara("id");
        if (StringUtil.isNotEmpty(id)) {
            SysUserFormMap sysUserFormMap = new SysUserFormMap();
            sysUserFormMap.put("id", id);
            List<SysUserFormMap> list = sysUserMapper.findDetailUser(sysUserFormMap);
            sysUserFormMap = list.get(0);
            sysUserFormMap.put("password", encrypt.decoder(list.get(0).getStr("password")));
            model.addAttribute("user", sysUserFormMap);
        }
        return ForwardConstants.SYSTEM + ForwardConstants.USER + ForwardConstants.EDIT;
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
    @SystemLog(module = "系统管理", methods = "用户管理-修改用户")
    /** 凡需要处理业务逻辑的.都需要记录操作日志*/
    public String editEntity(String txtGroupsSelect) throws Exception {
        if (txtGroupsSelect == null || txtGroupsSelect.equals("")) {
            String mytxtGroupsSelect="null";
            return mytxtGroupsSelect;
        } else {
            SysUserFormMap userFormMap = getFormMap(SysUserFormMap.class);
            if (userFormMap.get("locked").equals("1")) {
                String id = userFormMap.get("userId").toString();
                if (id.equals(SessionUtil.getUserIdSession())) {
                    return AttrConstants.FAIL;
                }
            }
            userFormMap.put("txtGroupsSelect", txtGroupsSelect);
            userFormMap.put("password", encrypt.encoder(userFormMap.getStr("password")));
            sysUserMapper.editEntity(userFormMap);
            sysUserRoleMapper.deleteByAttribute("userId", userFormMap.getStr("userId"), SysUserRoleFormMap.class);;
            if (!StringUtil.isEmpty(txtGroupsSelect)) {
                String[] txt = txtGroupsSelect.split(",");
                for (String roleId : txt) {
                    SysUserRoleFormMap userGroupsFormMap = new SysUserRoleFormMap();
                    userGroupsFormMap.put("userId", userFormMap.get("userId"));
                    userGroupsFormMap.put("roleId", roleId);
                    sysUserRoleMapper.addEntity(userGroupsFormMap);
                }
            }
            return AttrConstants.SUCCESS;
        }

    }

    /**
     * 验证账号是否存在
     * 
     * @param name
     * @return
     */
    @RequestMapping("isExist")
    @ResponseBody
    public boolean isExist(String userId, String userName) {
        SysUserFormMap sysUserFormMap = new SysUserFormMap();
        sysUserFormMap.put("userId", userId);
        sysUserFormMap.put("userName", userName);
        List<SysUserFormMap> user = sysUserMapper.count(sysUserFormMap);
        int count = Integer.parseInt(user.get(0).get("count").toString());
        if (count > 0) {
            return false;
        } else {
            return true;
        }
    }

    /** 密码修改*/
    @RequestMapping("updatePassword")
    public String updatePassword(Model model) throws Exception {
        return ForwardConstants.SYSTEM + ForwardConstants.USER + ForwardConstants.UPDATE_PASSWORD;
    }

    /** 保存新密码*/
    @RequestMapping("editPassword")
    @ResponseBody
    @SystemLog(module = "系统管理", methods = "用户管理-修改密码")
    /** 凡需要处理业务逻辑的.都需要记录操作日志*/
    public String editPassword() throws Exception {
        // 当验证都通过后，把用户信息放在session里
        SysUserFormMap sysUserFormMap = getFormMap(SysUserFormMap.class);
        String userName = sysUserFormMap.getStr("userName");
        SysUserFormMap oneUser = new SysUserFormMap();
        oneUser.put("userName", userName);
        List<SysUserFormMap> dataOne = sysUserMapper.findByNames(oneUser);
        // 这里对修改的密码进行加密
        String password = encrypt.encoder(sysUserFormMap.getStr("newpassword"));
        if(dataOne.size()>0){
            sysUserFormMap.put("password",password);
            sysUserMapper.editEntity(sysUserFormMap);
        }
        return AttrConstants.SUCCESS;
    }
    @ResponseBody
    @RequestMapping("getuser")
    public List<SysUserFormMap> getuser(String jName){
	SysUserFormMap formMap = new SysUserFormMap();
	if (StringUtil.isEmpty(jName)) {
	formMap.put("where", "where DELETE_STATUS='0' and USER_NAME !='"+SessionUtil.getSessionAttr("userName")+"'");
	}else {
	    formMap.put("where", "where DELETE_STATUS='0' and USER_NAME not in ("+jName+",'"+SessionUtil.getSessionAttr("userName")+"')");
	}
	return sysUserMapper.findByWhere(formMap);
	
    }
    /**
     * 获取角色下拉框
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("selUser")
    public String seletUser(Model model) throws Exception {
	SysUserFormMap formMap = getFormMap(SysUserFormMap.class);
        Object equipId = formMap.get("equipId");
        if (null != equipId) {
            List<SysUserFormMap> list = sysUserMapper.seletEquipUser(formMap);
            String ugid = "";
            for (SysUserFormMap ml : list) {
                ugid += ml.get("userId") + ",";
            }
            ugid = StringUtil.removeEnd(ugid);
            model.addAttribute("txtUserSelect", ugid);
            model.addAttribute("equipUser", list);
            if (StringUtils.isNotBlank(ugid)) {
        	formMap.put("where", " where USER_ID not in (" + ugid + ") AND DELETE_STATUS = 0");
            }
            List<SysUserFormMap> users = sysUserMapper.findByWhere(formMap);
            model.addAttribute("user", users);
            return ForwardConstants.API + ForwardConstants.EQUIPMENT + ForwardConstants.USER_SELECT;
        } else {
            formMap.put("where", " where  DELETE_STATUS = 0");
            List<SysUserFormMap> users = sysUserMapper.findByWhere(formMap);
            model.addAttribute("user", users);
            return ForwardConstants.API + ForwardConstants.EQUIPMENT + ForwardConstants.USER_SELECT;
        }

    }

}