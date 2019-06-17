package com.ysttench.application.htbw.settings.web.controller.api;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

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
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.htbw.core.rdto.Jzchange;
import com.ysttench.application.htbw.core.rdto.SocketClient;
import com.ysttench.application.htbw.settings.kernel.entity.EquipmentFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.EquipmentMsgFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.EquipmentUserFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.EquiptypeFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.RegionalFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.WitchFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.EquipmentMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.EquipmentMsgMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.EquipmentUserMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.EquiptypeMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.RegionalMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.WitchMapper;
import com.ysttench.application.htbw.settings.web.controller.common.AttrConstants;
import com.ysttench.application.htbw.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.htbw.settings.web.controller.index.BaseController;
import com.ysttench.application.htbw.settings.web.listener.InitMapperListener;

@Controller
@RequestMapping("/equipment/")
public class EquipmentController extends BaseController {
    @Inject
    private EquipmentMapper equipmentMapper;
    @Inject
    private EquipmentMsgMapper equipmentMsgMapper;
    @Inject
    private EquipmentUserMapper equipmentUserMapper;
    @Inject
    private RegionalMapper regionalMapper;
    @Inject
    private EquiptypeMapper equiptypeMapper;
    @Inject
    private WitchMapper witchMapper;
    public static List<SocketClient> socketlist = InitMapperListener.socketlist;
    Jzchange Jzchange = new Jzchange();

    /**
     * 跳转列表页面
     * 
     * @return
     */
    @RequestMapping("list")
    public String listUI() {
	return ForwardConstants.API + ForwardConstants.EQUIPMENT + ForwardConstants.LIST;
    }

    /**
     * 分页显示
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
	List<EquipmentFormMap> list = equipmentMapper.findByWhere(new EquipmentFormMap());
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	for (EquipmentFormMap map : list) {
	    if (new Date().getTime() > formatter.parse(map.get("checkDate").toString()).getTime()) {
		Date date = formatter.parse(map.get("checkDate").toString());
		WitchFormMap witchFormMap = new WitchFormMap();
		witchFormMap.put("where", "where WITCH_CODE='JY001' and EQUIPMENT_ID='" + map.get("id").toString()
			+ "' and WITCH_DATE > '" + formatter.format(date) + "'");
		List<WitchFormMap> witchlist = witchMapper.findByWhere(witchFormMap);
		if (witchlist.size() == 0) {
		    witchFormMap.put("wDesc", "设备校验时间超时");
		    witchFormMap.put("witchDate", DatetimeUtil.getDate());
		    witchFormMap.put("witchCode", "JY001");
		    witchFormMap.put("equipmentId", map.get("id").toString());
		    witchMapper.addEntity(witchFormMap);
		}
	    }
	}
	EquipmentFormMap formMap = getFormMap(EquipmentFormMap.class);
	formMap = toFormMap(formMap, pageNow, pageSize, formMap.getStr("orderby"));
	formMap.put("column", StringUtil.prop2tablefield(column));
	formMap.put("sort", sort);
	pageView.setRecords(equipmentMapper.findEquipPage(formMap));
	return pageView;
    }

    /**
     * 跳转新增页面
     * 
     * @return
     * @throws Exception
     */
    @RequestMapping("addUI")
    public String addUI() throws Exception {
	return ForwardConstants.API + ForwardConstants.EQUIPMENT + ForwardConstants.ADD;
    }

    /**
     * 设备新增
     * 
     * @param txtGroupsSelect
     * @return
     */
    @ResponseBody
    @RequestMapping("addEntity")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    @SystemLog(module = "设备管理", methods = "设备管理-新增设备")
    // 需要事务操作必须加入此注解
    @Transactional(readOnly = false)
    public String addEntity(String txtGroupsSelect) {
	try {
	    if (txtGroupsSelect == null || txtGroupsSelect.equals("")) {
		return AttrConstants.FAIL;
	    } else {
		EquipmentFormMap formMap = getFormMap(EquipmentFormMap.class);
		equipmentMapper.addEntity(formMap);
		if (!StringUtil.isEmpty(txtGroupsSelect)) {
		    String[] txt = txtGroupsSelect.split(",");
		    EquipmentUserFormMap equipGroupsFormMap = new EquipmentUserFormMap();
		    for (String userId : txt) {
			equipGroupsFormMap.put("userId", userId);
			equipGroupsFormMap.put("equipmentId", formMap.get("id"));
			equipmentUserMapper.addEntity(equipGroupsFormMap);
		    }
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SystemException("添加异常");
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 设备删除
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteEntity")
    // 需要事务操作必须加入此注解
    @Transactional(readOnly = false)
    // 凡需要处理业务逻辑的.都需要记录操作日志
    @SystemLog(module = "设备管理", methods = "设备管理-删除设备")
    public String deleteEntity() {
	try {
	    String[] ids = SessionUtil.getParaValues("ids");
	    String[] ids1 = ids[0].split(",");
	    for (String id : ids1) {
		equipmentMapper.deleteByAttribute("id", id, EquipmentFormMap.class);
		equipmentUserMapper.deleteByAttribute("equipmentId", id, EquipmentUserFormMap.class);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SystemException("删除异常");
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 进入编辑界面
     * 
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("editUI")
    public String editUI(Model model) throws Exception {
	String id = SessionUtil.getPara("id");
	if (StringUtil.isNotEmpty(id)) {
	    EquipmentFormMap map = new EquipmentFormMap();
	    map.put("id", id);
	    map = equipmentMapper.findDetail(map).get(0);
	    model.addAttribute("obj", map);
	}
	return ForwardConstants.API + ForwardConstants.EQUIPMENT + ForwardConstants.EDIT;
    }

    /**
     * 编辑保存
     * 
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("editEntity")
    // 凡需要处理业务逻辑的.都需要记录操作日志
    @SystemLog(module = "设备管理", methods = "设备管理-修改设备")
    public String editEntity(String txtGroupsSelect) throws Exception {
	if (txtGroupsSelect == null || txtGroupsSelect.equals("")) {
	    String mytxtGroupsSelect = "null";
	    return mytxtGroupsSelect;
	} else {
	    EquipmentFormMap formMap = getFormMap(EquipmentFormMap.class);
	    formMap.put("monitor", "1");
	    equipmentMapper.editEntity(formMap);
	    equipmentUserMapper.deleteByAttribute("equipmentId", formMap.getStr("id"), EquipmentUserFormMap.class);
	    if (!StringUtil.isEmpty(txtGroupsSelect)) {
		String[] txt = txtGroupsSelect.split(",");
		EquipmentUserFormMap equipGroupsFormMap = new EquipmentUserFormMap();
		for (String userId : txt) {
		    equipGroupsFormMap.put("userId", userId);
		    equipGroupsFormMap.put("equipmentId", formMap.get("id"));
		    equipmentUserMapper.addEntity(equipGroupsFormMap);
		}
	    }
	}
	try {
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new SystemException("编辑异常");
	}
	return AttrConstants.SUCCESS;
    }

    /**
     * 获取折线图信息
     * 
     * @param id
     * @param parentId
     * @param equipmentNum
     * @return
     */
    @ResponseBody
    @RequestMapping("datalist")
    public List<EquipmentFormMap> getEquipmentdata(String id, String equipmentName) throws Exception {
	EquipmentFormMap equipmentFormMap = new EquipmentFormMap();
	if (StringUtil.isEmpty(equipmentName)) {
	    if (StringUtil.isEmpty(id) || "all".equals(id)) {
		equipmentFormMap.put("where", "where (ENABLE_STATUS='1' OR ENABLE_STATUS='3')");
	    } else {
		String ids = "";
		RegionalFormMap map = regionalMapper.findbyFrist("id", id, RegionalFormMap.class);
		if ("0".equals(map.get("parentId").toString())) {
		    List<RegionalFormMap> list = regionalMapper.findByAttribute("parentId", id, RegionalFormMap.class);
		    for (RegionalFormMap regionalFormMap : list) {
			ids += regionalFormMap.get("id").toString() + ",";
		    }
		} else {
		    ids = id + ",";
		}
		equipmentFormMap.put("where", "where (ENABLE_STATUS='1' OR ENABLE_STATUS='3') AND DOMAIN IN ("
			+ ids.substring(0, ids.length() - 1) + ")");
	    }
	} else {
	    String name = new String(equipmentName.getBytes("8859_1"), "utf8");
	    if (StringUtil.isEmpty(id) || "all".equals(id)) {
		equipmentFormMap.put("where",
			"where (ENABLE_STATUS='1' OR ENABLE_STATUS='3') AND EQUIPMENT_NAME='" + name + "' ");
	    } else {
		String ids = "";
		RegionalFormMap map = regionalMapper.findbyFrist("id", id, RegionalFormMap.class);
		if ("0".equals(map.get("parentId").toString())) {
		    List<RegionalFormMap> list = regionalMapper.findByAttribute("parentId", id, RegionalFormMap.class);
		    for (RegionalFormMap regionalFormMap : list) {
			ids += regionalFormMap.get("id").toString() + ",";
		    }
		} else {
		    ids = id + ",";
		}
		equipmentFormMap.put("where", "where (ENABLE_STATUS='1' OR ENABLE_STATUS='3') AND DOMAIN IN ("
			+ ids.substring(0, ids.length() - 1) + ") AND EQUIPMENT_NAME='" + name + "'");
	    }
	}

	List<EquipmentFormMap> list = equipmentMapper.findByWhere(equipmentFormMap);
	return list;
    }

    @ResponseBody
    @RequestMapping("getdatalist")
    public List<EquipmentMsgFormMap> getdatalist(String id) throws Exception {
	EquipmentMsgFormMap map = new EquipmentMsgFormMap();
	map.put("equipmentId", id);
	return equipmentMapper.findChangeCurv(map);

    }

    /**
     * 获取区域图示信息
     * 
     * @param id
     * @param parentId
     * @param equipmentNum
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("getRelnfo")
    public List<EquipmentFormMap> getEquipMsg(String id, String equipmentName) throws Exception {
	List<EquipmentFormMap> showlist = new ArrayList<EquipmentFormMap>();
	EquipmentFormMap equipmentFormMap = new EquipmentFormMap();
	if (StringUtil.isEmpty(equipmentName)) {
	    if (StringUtil.isEmpty(id) || "all".equals(id)) {
		equipmentFormMap.put("where", "where (ENABLE_STATUS='1' OR ENABLE_STATUS='3')");
	    } else {
		String ids = "";
		RegionalFormMap map = regionalMapper.findbyFrist("id", id, RegionalFormMap.class);
		if ("0".equals(map.get("parentId").toString())) {
		    List<RegionalFormMap> list = regionalMapper.findByAttribute("parentId", id, RegionalFormMap.class);
		    for (RegionalFormMap regionalFormMap : list) {
			ids += regionalFormMap.get("id").toString() + ",";
		    }
		} else {
		    ids = id + ",";
		}
		equipmentFormMap.put("where", "where (ENABLE_STATUS='1' OR ENABLE_STATUS='3') AND DOMAIN IN ("
			+ ids.substring(0, ids.length() - 1) + ")");
	    }
	} else {
	    String name = new String(equipmentName.getBytes("8859_1"), "utf8");
	    if (StringUtil.isEmpty(id) || "all".equals(id)) {
		equipmentFormMap.put("where",
			"where (ENABLE_STATUS='1' OR ENABLE_STATUS='3') AND EQUIPMENT_NAME='" + name + "'");
	    } else {
		String ids = "";
		RegionalFormMap map = regionalMapper.findbyFrist("id", id, RegionalFormMap.class);
		if ("0".equals(map.get("parentId").toString())) {
		    List<RegionalFormMap> list = regionalMapper.findByAttribute("parentId", id, RegionalFormMap.class);
		    for (RegionalFormMap regionalFormMap : list) {
			ids += regionalFormMap.get("id").toString() + ",";
		    }
		} else {
		    ids = id + ",";
		}
		equipmentFormMap.put("where", "where (ENABLE_STATUS='1' OR ENABLE_STATUS='3') AND DOMAIN IN ("
			+ ids.substring(0, ids.length() - 1) + ") AND EQUIPMENT_NAME='" + name + "'");
	    }
	}
	List<EquipmentFormMap> list = equipmentMapper.findByWhere(equipmentFormMap);
	for (EquipmentFormMap map : list) {
	    EquipmentFormMap emap = new EquipmentFormMap();
	    emap.put("equipmentNum", map.get("equipmentNum").toString());
	    emap.put("equipmentName", map.get("equipmentName").toString());
	    EquipmentMsgFormMap msg = new EquipmentMsgFormMap();
	    msg.put("where", "where EQUIPMENT_ID='"+map.get("id").toString()+"' ORDER BY DATE DESC");
	    List<EquipmentMsgFormMap> msglist = equipmentMsgMapper.findByWhere(msg);
	    msg= msglist.get(0);
	    emap.put("time", msg.get("date").toString());
	    if (msg.get("tempValue") != null && msg.get("humValue")!= null) {
		emap.put("status", "0");
		emap.put("echartValue", msg.get("tempValue").toString());
		emap.put("rhechartValue", msg.get("humValue").toString());
	    }else {
		emap.put("status", "1");
		emap.put("echartValue", "");
		emap.put("rhechartValue", "");
	    }
	    showlist.add(emap);
	}
	return showlist;

    }

    /**
     * 启动监测
     */
    @ResponseBody
    @RequestMapping("keepsocket")
    public String keepsocket() {
	socketlist.clear();
	EquipmentFormMap map = new EquipmentFormMap();
	List<EquipmentFormMap> list = equipmentMapper.findEquipPage(map);
	for (int i = 0; i < list.size(); i++) {
	    EquipmentFormMap map1 = new EquipmentFormMap();
	    map1.put("id", list.get(i).get("id").toString());
	    EquiptypeFormMap typemap = equiptypeMapper.findbyFrist("id", list.get(i).get("equiptypeId").toString(),
		    EquiptypeFormMap.class);
	    SocketClient client = new SocketClient();
	    client.setTemLimt(typemap.getStr("temLimit"));
	    client.setHumLimit(typemap.getStr("humLimit"));
	    client.setVolLimit(typemap.getStr("volLimit"));
	    client.setEquipmentId(list.get(i).get("id").toString());
	    client.setEquipmentName(list.get(i).getStr("equipmentName"));
	    client.setEquipmentNum(list.get(i).getStr("equipmentNum"));
	    client.setAlarmMaxHumidity(list.get(i).getStr("alarmMaxHumidity"));
	    client.setAlarmMaxTemperature(list.get(i).getStr("alarmMaxTemperature"));
	    client.setAlarmMinHumidity(list.get(i).getStr("alarmMinHumidity"));
	    client.setAlarmMinTemperature(list.get(i).getStr("alarmMinTemperature"));
	    client.setDomainName(list.get(i).getStr("domainName"));
	    client.setEquipmentIp(list.get(i).getStr("equipmentIp"));
	    client.setEquiptypeName(list.get(i).getStr("equiptypeName"));
	    client.setEquipmentPort(list.get(i).getStr("wsPort"));
	    try {
		Socket socket = new Socket(list.get(i).get("equipmentIp").toString(),
			Integer.parseInt(list.get(i).get("wsPort").toString()));
		socket.setKeepAlive(true);
		socket.setSoTimeout(10);
		socket.sendUrgentData(0xFF);
		client.setStatus("0");
		client.setSocket(socket);
		map1.put("enableStatus", "1");
		map1.put("monitor", "0");
		equipmentMapper.editEntity(map1);
		socketlist.add(client);
	    } catch (Exception e) {
		client.setStatus("1");
		socketlist.add(client);
		WitchFormMap witchFormMap = new WitchFormMap();
		witchFormMap.put("wDesc", "连接超时");
		witchFormMap.put("witchCode", "GZ001");
		witchFormMap.put("witchDate", DatetimeUtil.getDate());
		witchFormMap.put("equipmentId", list.get(i).get("id").toString());
		try {
		    map1.put("enableStatus", "3");
		    map1.put("monitor", "1");
		    equipmentMapper.editEntity(map1);
		    witchMapper.addEntity(witchFormMap);
		} catch (Exception e1) {
		    e1.printStackTrace();
		}
		continue;
	    }
	}
	return AttrConstants.SUCCESS;

    }

    /**
     * 获取监控设备数量
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("jkNum")
    public int jkNum() {
	EquipmentFormMap map = new EquipmentFormMap();
	map.put("where", "where monitor = '0'");
	return equipmentMapper.findByWhere(map).size();

    }

    /**
     * 获取未监控设备数量
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("ycNum")
    public int ycNum() {
	EquipmentFormMap map = new EquipmentFormMap();
	map.put("where", "where monitor = '1'");
	return equipmentMapper.findByWhere(map).size();

    }
}
