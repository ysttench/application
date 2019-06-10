package com.ysttench.application.msd.settings.web.controller.api;

import java.net.Socket;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.common.annotation.SystemLog;
import com.ysttench.application.common.plugin.PageView;
import com.ysttench.application.common.server.SessionUtil;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.msd.settings.kernel.entity.ApiCabFormMap;
import com.ysttench.application.msd.settings.kernel.entity.ApiMsdQDFormMap;
import com.ysttench.application.msd.settings.kernel.mapper.ApiCabMapper;
import com.ysttench.application.msd.settings.kernel.mapper.ApiMsdQDMapper;
import com.ysttench.application.msd.settings.web.controller.common.AttrConstants;
import com.ysttench.application.msd.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.msd.settings.web.controller.index.BaseController;
import com.ysttench.application.msd.settings.web.controller.socket.TcpClient;
import com.ysttench.application.msd.settings.web.rdto.util.Modbus;

/**
 * msd清单
 * 
 * @author Howard
 *
 */
@Controller
@RequestMapping("/msdqd/")
public class ApiMsdQDController extends BaseController {
    @Inject
    ApiMsdQDMapper apiMsdQDMapper;
    @Inject
    ApiCabMapper apiCabMapper;

    @RequestMapping("list")
    public String listUI(Model model) throws Exception {
	return ForwardConstants.API + ForwardConstants.MSDQD + ForwardConstants.LIST;
    }

    @ResponseBody
    @RequestMapping("findByPage")
    public PageView findByPage(String pageNow, String pageSize, String column, String sort) throws Exception {
	ApiMsdQDFormMap apiMsdQDFormMap = getFormMap(ApiMsdQDFormMap.class);
	if (StringUtil.isEmpty(apiMsdQDFormMap.getStr("flot"))) {
	    apiMsdQDFormMap.remove("flot");
	}
	apiMsdQDFormMap = toFormMap(apiMsdQDFormMap, pageNow, pageSize, apiMsdQDFormMap.getStr("orderby"));
	apiMsdQDFormMap.put("column", StringUtil.prop2tablefield(column));
	apiMsdQDFormMap.put("sort", sort);
	pageView.setRecords(apiMsdQDMapper.findMSDQDPage(apiMsdQDFormMap));// 不调用默认分页,调用自已的mapper中findUserPage
	return pageView;
    }

    /**
     * 控制
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("takEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "msd管理", methods = "msd管理-取用元件")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String takEntity() throws Exception {
	String[] ids = SessionUtil.getParaValues("ids");
	String[] ids1 = ids[0].split(",");
	for (String id : ids1) {
	    ApiMsdQDFormMap map = new ApiMsdQDFormMap();
	    map.put("id", id);
	    List<ApiMsdQDFormMap> list = apiMsdQDMapper.findModbusMsg(map);
	    if (list.size() != 0) {
		map = list.get(0);
		if (Integer.parseInt(map.getStr("resiLife")) <= 0) {
		    return AttrConstants.LIFEOUT;
		} else if ("1".equals(map.get("requestStatus").toString())) {
		    return AttrConstants.AREADYREQUEST;
		} else if ("2".equals(map.get("requestStatus").toString())) {
		    return AttrConstants.AREADYGET;
		} else {
		    Modbus modbus = new Modbus(map.get("ip").toString(), Integer.parseInt(map.get("port").toString()));
		    modbus.writeCoil(Integer.parseInt(map.get("point").toString()), true);
		    Modbus ledmodbus = new Modbus(map.get("ledIp").toString(),
			    Integer.parseInt(map.get("ledPort").toString()));
		    ledmodbus.writeCoil(Integer.parseInt(map.get("ledPoint").toString()), true);
		    TcpClient client = new TcpClient();
		    client.sendTcpMessage(map.get("ledIp").toString(), Integer.parseInt(map.get("ledBport").toString()),
			    map.get("arkCodee").toString());
		    map.put("requestStatus", "1");
		    apiMsdQDMapper.editEntity(map);
		}
	    } else {
		return AttrConstants.FAIL;
	    }

	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 控制
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("takoverEntity")
    @Transactional(readOnly = false)
    // 需要事务操作必须加入此注解
    @SystemLog(module = "msd管理", methods = "msd管理-取用完成")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    public String takoverEntity() throws Exception {
	String[] ids = SessionUtil.getParaValues("ids");
	String[] ids1 = ids[0].split(",");
	for (String id : ids1) {
	    ApiMsdQDFormMap map = new ApiMsdQDFormMap();
	    map.put("id", id);
	    List<ApiMsdQDFormMap> list = apiMsdQDMapper.findModbusMsg(map);
	    if (list.size() != 0) {
		map = list.get(0);
		if (!"1".equals(map.get("requestStatus").toString())) {
		    return AttrConstants.FAIL1;
		} else {
		    Modbus modbus = new Modbus(map.get("ip").toString(), Integer.parseInt(map.get("port").toString()));
		    modbus.writeCoil(Integer.parseInt(map.get("point").toString()), false);
		    Modbus ledmodbus = new Modbus(map.get("ledIp").toString(),
			    Integer.parseInt(map.get("ledPort").toString()));
		    ledmodbus.writeCoil(Integer.parseInt(map.get("ledPoint").toString()), false);
		    map.put("requestStatus", "2");
		    ApiCabFormMap formMap = new ApiCabFormMap();
		    formMap.put("id", map.getStr("arkCode"));
		    formMap.put("status", "0");
		    apiCabMapper.editEntity(formMap);
		    ApiCabFormMap cabmap = apiCabMapper.findbyFrist("arkName", "手推车", ApiCabFormMap.class);
		    map.put("arkCode", cabmap.get("id").toString());
		    apiMsdQDMapper.editEntity(map);
		}
	    } else {
		return AttrConstants.FAIL;
	    }

	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 控制
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("returnEntity")
    @Transactional(readOnly = false)
// 需要事务操作必须加入此注解
    @SystemLog(module = "msd管理", methods = "msd管理-取用还原")
// 凡需要处理业务逻辑的.都需要记录操作日志
    public String returnEntity() throws Exception {
	String[] ids = SessionUtil.getParaValues("ids");
	String[] ids1 = ids[0].split(",");
	for (String id : ids1) {
	    ApiMsdQDFormMap map = new ApiMsdQDFormMap();
	    map.put("id", id);
	    List<ApiMsdQDFormMap> list = apiMsdQDMapper.findByAttribute("id", id, ApiMsdQDFormMap.class);
	    if (list.size() != 0) {
		map = list.get(0);
		ApiCabFormMap formMap = new ApiCabFormMap();
		formMap.put("id", map.getStr("arkCode"));
		formMap.put("status", "0");
		apiCabMapper.editEntity(formMap);
		map.put("requestStatus", "0");
		ApiCabFormMap cabmap = apiCabMapper.findbyFrist("arkName", "手推车", ApiCabFormMap.class);
		map.put("arkCode", cabmap.get("id").toString());
		apiMsdQDMapper.editRequestStatus(map);
	    }
	}
	return AttrConstants.SUCCESS;
    }

    @ResponseBody
    @RequestMapping("keepUI")
    public String keepUI(Model model) throws Exception {
	String[] ids = SessionUtil.getParaValues("ids");
	String[] ids1 = ids[0].split(",");
	for (String id : ids1) {
	    List<ApiMsdQDFormMap> list = apiMsdQDMapper.findByAttribute("id", id, ApiMsdQDFormMap.class);
	    ApiMsdQDFormMap map = list.get(0);
	    if (!StringUtil.isEmpty(map.getStr("arkCode"))) {
		List<ApiCabFormMap> alist1 = apiCabMapper.findByAttribute("id", map.getStr("arkCode"),
			ApiCabFormMap.class);
		ApiCabFormMap map1 = alist1.get(0);
		if ("1".equals(map1.get("arkType")) || "2".equals(map1.get("arkType"))) {
		    return AttrConstants.FAIL;
		}
	    }
	    ApiCabFormMap formMap = new ApiCabFormMap();
	    if (Integer.parseInt(map.getStr("resiLife")) <= 0) {
		formMap.put("where", "where STATUS=0 AND ARK_TYPE=2 order by ID");
	    } else {
		formMap.put("where", "where STATUS=0 AND ARK_TYPE=1 order by ID");
	    }
	    List<ApiCabFormMap> alist = apiCabMapper.findByWhere(formMap);
	    if (alist.size() == 0) {
		return AttrConstants.FAIL1;
	    }
	    formMap = alist.get(0);
	    Modbus modbus = new Modbus(formMap.get("ip").toString(), Integer.parseInt(formMap.get("port").toString()));
	    modbus.writeCoil(Integer.parseInt(formMap.get("point").toString()), true);
	    ApiCabFormMap cabmap = apiCabMapper.findbyFrist("id", formMap.get("upCab").toString(), ApiCabFormMap.class);
	    Modbus ledmodbus = new Modbus(cabmap.get("ip").toString(), Integer.parseInt(cabmap.get("port").toString()));
	    ledmodbus.writeCoil(Integer.parseInt(cabmap.get("point").toString()), true);
	    TcpClient client = new TcpClient();
	    client.sendTcpMessage(cabmap.get("ledIp").toString(), Integer.parseInt(cabmap.get("ledPort").toString()),
		    formMap.get("arkCode").toString());
	    map.put("arkCode", formMap.get("id").toString());
	    apiMsdQDMapper.editEntity(map);
	    formMap.put("status", "1");
	    apiCabMapper.editEntity(formMap);
	}
	return AttrConstants.SUCCESS;
    }

    @ResponseBody
    @RequestMapping("saveUI")
    public String saveUI(Model model) throws Exception {
	String id = SessionUtil.getPara("id");
	if (StringUtil.isNotEmpty(id)) {
	    ApiMsdQDFormMap map = apiMsdQDMapper.findByAttribute("id", id, ApiMsdQDFormMap.class).get(0);
	    List<ApiCabFormMap> alist = apiCabMapper.findByAttribute("id", map.getStr("arkCode"), ApiCabFormMap.class);
	    ApiCabFormMap formMap = alist.get(0);
	    ApiCabFormMap cabmap = apiCabMapper.findbyFrist("id", formMap.get("upCab").toString(), ApiCabFormMap.class);
	    Modbus modbus = new Modbus(formMap.get("ip").toString(), Integer.parseInt(formMap.get("port").toString()));
	    modbus.writeCoil(Integer.parseInt(formMap.get("point").toString()), false);
	    Modbus ledmodbus = new Modbus(cabmap.get("ip").toString(), Integer.parseInt(cabmap.get("port").toString()));
	    ledmodbus.writeCoil(Integer.parseInt(cabmap.get("point").toString()), false);
	}
	return AttrConstants.SUCCESS;
    }

    @ResponseBody
    @RequestMapping("qtakEntity")
    public String qtakEntity(Model model) throws Exception {
	String id = SessionUtil.getPara("id");
	if (StringUtil.isNotEmpty(id)) {
	    ApiMsdQDFormMap map = apiMsdQDMapper.findByAttribute("id", id, ApiMsdQDFormMap.class).get(0);
	    List<ApiCabFormMap> alist = apiCabMapper.findByAttribute("id", map.getStr("arkCode"), ApiCabFormMap.class);
	    ApiCabFormMap formMap = alist.get(0);
	    ApiCabFormMap camap = apiCabMapper.findbyFrist("id", formMap.get("upCab").toString(), ApiCabFormMap.class);
	    Modbus modbus = new Modbus(formMap.get("ip").toString(), Integer.parseInt(formMap.get("port").toString()));
	    modbus.writeCoil(Integer.parseInt(formMap.get("point").toString()), true);
	    Modbus ledmodbus = new Modbus(camap.get("ip").toString(),
		    Integer.parseInt(camap.get("port").toString()));
	    ledmodbus.writeCoil(Integer.parseInt(camap.get("point").toString()), true);
	    TcpClient client = new TcpClient();
	    client.sendTcpMessage(camap.get("ledIp").toString(), Integer.parseInt(camap.get("ledPort").toString()),
		    formMap.get("arkCode").toString());
	    map.put("requestStatus", "1");
	    formMap.put("status", "0");
	    apiCabMapper.editEntity(formMap);
	    apiMsdQDMapper.editEntity(map);
	}
	return AttrConstants.SUCCESS;
    }
}
