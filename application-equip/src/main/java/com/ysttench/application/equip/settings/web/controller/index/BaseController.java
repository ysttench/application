package com.ysttench.application.equip.settings.web.controller.index;

import java.util.Enumeration;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.HtmlUtil;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.database.ibatis.entity.FormMap;
import com.ysttench.application.equip.settings.kernel.entity.SysMenuFormMap;
import com.ysttench.application.equip.settings.kernel.entity.SysUserFormMap;
import com.ysttench.application.equip.settings.kernel.mapper.SysMenuMapper;

/**
 * 
 * @author howard
 */
public class BaseController {

    // 本地异常日志记录对象
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private SysMenuMapper sysMenuMapper;

    public PageView pageView = null;

    public PageView getPageView(String pageNow, String pageSize, String orderby) {
        if (StringUtil.isEmpty(pageNow)) {
            pageView = new PageView(1);
        } else {
            pageView = new PageView(Integer.parseInt(pageNow));
        }
        if (StringUtil.isEmpty(pageSize)) {
            pageSize = "10";
        }
        pageView.setPageSize(Integer.parseInt(pageSize));
        pageView.setOrderby(orderby);
        return pageView;
    }

    public <T> T toFormMap(T t, String pageNow, String pageSize, String orderby) {
        @SuppressWarnings("unchecked")
        FormMap<String, Object> formMap = (FormMap<String, Object>) t;
        formMap.put("paging", getPageView(pageNow, pageSize, orderby));
        return t;
    }

    /**
     * 获取返回某一页面的按扭组,
     * 
     * @return Class<T>
     * @throws Exception
     */
    public List<SysMenuFormMap> findRoleMenu() {
        // 资源ID
        String id = SessionUtil.getPara("id");
        // 通过工具类获取当前登录的bean
        SysUserFormMap sysUserFormMap = (SysUserFormMap) SessionUtil.getUserSession();
        // user id
        int userId = sysUserFormMap.getInt("userId");
        SysMenuFormMap menuQueryForm = new SysMenuFormMap();
        menuQueryForm.put("parentId", id);
        menuQueryForm.put("userId", userId);
        List<SysMenuFormMap> listSysMenuFormMap = sysMenuMapper.findRoleMenu(menuQueryForm);
        // List<ResFormMap> rse = sysMenuMapper.findByAttribute("parentId",
        // id, ResFormMap.class);
        for (SysMenuFormMap sysMenuFormMap : listSysMenuFormMap) {
            Object o = sysMenuFormMap.get("description");
            if (o != null && !StringUtil.isEmpty(o.toString())) {
                sysMenuFormMap.put("description", HtmlUtil.stringtohtml(o.toString()));
            }
        }
        return listSysMenuFormMap;
    }

    /**
     * 获取传递的所有参数, 反射实例化对象，再设置属性值 通过泛型回传对象.
     * 
     * @return Class<T>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T getFormMap(Class<T> clazz) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Enumeration<String> en = request.getParameterNames();
        T t = null;
        try {
            t = clazz.newInstance();
            FormMap<String, Object> map = (FormMap<String, Object>) t;
            String order = "", sort = "";
            while (en.hasMoreElements()) {
                String nms = en.nextElement().toString();
                if (nms.endsWith("[]")) {
                    String[] as = request.getParameterValues(nms);
                    if (as != null && as.length != 0 && as.toString() != "[]") {
                        String mname = t.getClass().getSimpleName().toUpperCase();
                        if (nms.toUpperCase().startsWith(mname)) {
                            nms = nms.substring(nms.toUpperCase().indexOf(mname) + 1);
                            map.put(nms, as);
                        }
                    }
                } else {
                    String as = request.getParameter(nms);
                    // 画面输入为空时 丢失
                    // if (!StringUtil.isEmpty(as)) {
                    String mname = t.getClass().getSimpleName().toUpperCase();
                    if (nms.toUpperCase().startsWith(mname)) {
                        nms = nms.substring(mname.length() + 1);
                        map.put(nms, as);
                    }
                    if (nms.toLowerCase().equals("column"))
                        order = as;
                    if (nms.toLowerCase().equals("sort"))
                        sort = as;
                    // }
                }
            }
            if (!StringUtil.isEmpty(order) && !StringUtil.isEmpty(sort))
                map.put("orderby", " order by " + StringUtil.prop2tablefield(order) + " " + sort);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获取传递的所有参数, 再设置属性值 通过回传Map对象. <br/>
     * <b>author：</b><br/>
     * <b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsplijianning</b><br/>
     * <b>date：</b><br/>
     * <b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp2015-04-01</b><br/>
     * <b>version：</b><br/>
     * <b>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp1.0v</b>
     * 
     * @return Class<T>
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T findHasHMap(Class<T> clazz) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Enumeration<String> en = request.getParameterNames();
        T t = null;
        try {
            t = clazz.newInstance();
            FormMap<String, Object> map = (FormMap<String, Object>) t;
            while (en.hasMoreElements()) {
                String nms = en.nextElement().toString();
                if (!"_t".equals(nms)) {
                    if (nms.endsWith("[]")) {
                        String[] as = request.getParameterValues(nms);
                        if (as != null && as.length != 0 && as.toString() != "[]") {
                            map.put(nms, as);
                        }
                    } else {
                        String as = request.getParameter(nms);
                        if (!StringUtil.isEmpty(as)) {
                            map.put(nms, as);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 获取传递的所有参数, 反射实例化对象，再设置属性值 通过泛型回传对象. 存在文件上传的时候
     * 
     * @return Class<T>
     * @throws Exception
     */
    public <T> T getFormMapHasFile(Class<T> clazz) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        Enumeration<String> en = request.getParameterNames();
        T t = null;
        try {
            t = clazz.newInstance();
            @SuppressWarnings("unchecked")
            FormMap<String, Object> map = (FormMap<String, Object>) t;
            String order = "", sort = "";
            while (en.hasMoreElements()) {
                String nms = en.nextElement().toString();
                if (nms.endsWith("[]")) {
                    String[] as = this.tranISO2utf8(request.getParameterValues(nms));
                    if (as != null && as.length != 0 && as.toString() != "[]") {
                        String mname = t.getClass().getSimpleName().toUpperCase();
                        if (nms.toUpperCase().startsWith(mname)) {
                            nms = nms.substring(mname.length() + 1);
                            map.put(nms, as);
                        }
                    }
                } else {
                    String as = new String(request.getParameter(nms).getBytes("ISO-8859-1"), "utf-8");
                    // 画面输入为空时 丢失
                    String mname = t.getClass().getSimpleName().toUpperCase();
                    if (nms.toUpperCase().startsWith(mname)) {
                        nms = nms.substring(mname.length() + 1);
                        map.put(nms, as);
                    }
                    if (nms.toLowerCase().equals("column"))
                        order = as;
                    if (nms.toLowerCase().equals("sort"))
                        sort = as;
                }
            }
            if (!StringUtil.isEmpty(order) && !StringUtil.isEmpty(sort))
                map.put("orderby", " order by " + StringUtil.prop2tablefield(order) + " " + sort);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 文件上传的时候，同事有表单时，要做iso转utf-8
     * 
     * @param params
     * @return
     * @throws Exception
     */
    private String[] tranISO2utf8(String[] params) throws Exception {
        String[] returnValues = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            returnValues[i] = new String(params[i].getBytes("ISO-8859-1"), "utf-8");
        }
        return returnValues;
    }
}