package com.ysttench.application.htbw.settings.web.listener;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.ysttench.application.common.server.ConfigUtils;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.core.rdto.Jzchange;
import com.ysttench.application.htbw.core.rdto.SocketClient;
import com.ysttench.application.htbw.settings.kernel.entity.EquipmentFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.EquipmentMsgFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.EquiptypeFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.WarnMsgFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.WitchFormMap;
import com.ysttench.application.htbw.settings.kernel.mapper.EquipmentMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.EquipmentMsgMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.EquipmentUserMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.EquiptypeMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.WarnMsgMapper;
import com.ysttench.application.htbw.settings.kernel.mapper.WitchMapper;

/**
 * 
 * @author Howard
 */
@Repository
public class InitMapperListener implements ServletContextListener {
    @Inject
    EquipmentUserMapper equipmentUserMapper;
    @Inject
    private EquipmentMsgMapper equipmentMsgMapper;
    @Inject
    private WarnMsgMapper warnMsgMapper;
    @Inject
    private EquiptypeMapper equiptypeMapper;
    @Inject
    private WitchMapper witchMapper;
    private static Log logger = LogFactory.getLog(InitMapperListener.class);
    @Inject
    private BaseMapper baseMapper;
    // 从 application.properties 中读取配置
    @Value("${spring.datasource.url}")
    private String url = "";

    @Value("${spring.datasource.username}")
    private String username = "";

    @Value("${spring.datasource.dialect}")
    private String dialect = "";

    @Value("${spring.datasource.mybatis.entity}")
    private String entity = "";

    @Inject
    EquipmentMapper equipmentMapper;
    public static List<SocketClient> socketlist = new ArrayList<SocketClient>();

    public void contextInitialized(ServletContextEvent sce) {
	logger.info("配置信息初始化Start!!!");
	if ("mysql".equalsIgnoreCase(dialect)) {
	    String db = url.substring(url.lastIndexOf("/") + 1);
	    if (db.indexOf("?") > -1) {
		db = db.substring(0, db.indexOf("?"));
	    }
	    ConfigUtils.initTableFieldForMysql(baseMapper, entity, db);
	    try {
		TimerSocketClient();
		TimerSocketMsg();
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	} else if ("oracle".equalsIgnoreCase(dialect)) {
	    ConfigUtils.initTableFieldForOracle(baseMapper, entity, username);
	}
	logger.info("配置信息初始化End!!!");
    }

    /**
     * 建立定时器 定时验证socket的连接状态
     */
    public void TimerSocketClient() {
	keepsocket();
	synchronized (InitMapperListener.class) {
	    Timer timer = new Timer();
	    timer.schedule(new TimerTask() {
		@Override
		public void run() {
		    List<SocketClient> socklist = new ArrayList<SocketClient>();
		    if (socketlist.size() != 0) {
			for (SocketClient socketClient : socketlist) {
			    EquipmentFormMap map = new EquipmentFormMap();
			    map.put("id", socketClient.getEquipmentId());
			    if ("0".equals(socketClient.getStatus())) {
				Socket socket = socketClient.getSocket();
				try {
				    socket.setKeepAlive(true);
				    socket.setSoTimeout(10);
				    socket.sendUrgentData(0xFF);
				    socklist.add(socketClient);
				} catch (Exception e) {
				    map.put("enableStatus", "3");
				    map.put("monitor", "1");
				    try {
					equipmentMapper.editEntity(map);
				    } catch (Exception e1) {
					e1.printStackTrace();
					continue;
				    }
				    socketClient.setStatus("1");
				    socklist.add(socketClient);
				    continue;
				}
			    } else {
				try {
				    Socket socket = new Socket(socketClient.getEquipmentIp(),
					    Integer.parseInt(socketClient.getEquipmentPort()));
				    socket.setKeepAlive(true);
				    socket.setSoTimeout(10);
				    socket.sendUrgentData(0xFF);
				    socketClient.setSocket(socket);
				    socketClient.setStatus("0");
				    socklist.add(socketClient);
				    map.put("enableStatus", "1");
				    map.put("monitor", "0");
				    try {
					equipmentMapper.editEntity(map);
				    } catch (Exception e1) {
					e1.printStackTrace();
					continue;
				    }
				} catch (Exception e) {
				    socklist.add(socketClient);
				    continue;
				}
			    }
			}
			socketlist.clear();
			for (SocketClient client1 : socklist) {
			    socketlist.add(client1);
			}
		    }
		}

	    }, 1000, 20000);
	}
    }

    public void contextDestroyed(ServletContextEvent arg0) {
	logger.info("配置信息清除!!!");
    }

    /**
     * 启动监测
     */
    public void keepsocket() {
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

    }

    /**
     * 建立计时器定时监控数据
     * 
     * @throws Exception
     */
    public void TimerSocketMsg() throws Exception {
	Jzchange Jzchange = new Jzchange();
	synchronized (InitMapperListener.class) {
	    Timer timer = new Timer("socketimer");
	    timer.schedule(new TimerTask() {
		@Override
		public void run() {
		    if (socketlist.size() != 0) {
			for (int i = 0; i < socketlist.size(); i++) {
			    SocketClient client = socketlist.get(i);
			    try {
				int temLimit = Integer.parseInt(client.getTemLimt());
				int humLimit = Integer.parseInt(client.getHumLimit());
				if ("0".equals(client.getStatus())) {
				    Socket socket = client.getSocket();
				    socket.setKeepAlive(true);
				    socket.setSoTimeout(10);
				    socket.sendUrgentData(0xFF);
				    InputStream inputStream = socket.getInputStream();
				    Double tempValue = 0.0;
				    Double humValue = 0.0;
				    String s = null;
				    byte[] buffer = new byte[1024];
				    inputStream.read(buffer);
				    if ("F149".equals(client.getEquiptypeName())
					    || "F249".equals(client.getEquiptypeName())
					    || "CP09U".equals(client.getEquiptypeName())
					    || "F349".equals(client.getEquiptypeName())) {
					String msg = Jzchange.BinaryToHexString(buffer);
					String msgs = msg.substring(msg.indexOf("ABAB"), msg.length());
					s = msgs.substring(0, msgs.indexOf("0D0A") + 4);
					if (!StringUtil.isEmpty(s)) {
					    tempValue = Jzchange
						    .HexStringToDecimal(s.substring(2 * temLimit, 2 * temLimit + 4));
					    humValue = Jzchange
						    .HexStringToDecimal(s.substring(2 * humLimit, 2 * humLimit + 4));
					}
				    } else if("HT-2".equals(client.getEquiptypeName())){
					s = Jzchange.hexStr2Str(Jzchange.BinaryToHexString(buffer));
					if (!StringUtil.isEmpty(s)) {
					    tempValue = Double.parseDouble(
						    s.substring(s.indexOf("temp") + 5, s.indexOf("&humi")));
					    humValue = Double.parseDouble(
						    s.substring(s.indexOf("humi") + 5, s.indexOf("&Time")));
					}
				    }
				    if (tempValue != 0.0 && humValue != 0.0) {
					EquipmentMsgFormMap equipmentmsgFormMap = new EquipmentMsgFormMap();
					equipmentmsgFormMap.put("tempValue", tempValue);
					equipmentmsgFormMap.put("humValue", humValue);
					equipmentmsgFormMap.put("equipmentId", client.getEquipmentId());
					equipmentmsgFormMap.put("date", DatetimeUtil.getDate());
					equipmentMsgMapper.addEntity(equipmentmsgFormMap);
				    }
				    List<WarnMsgFormMap> msglist = new ArrayList<WarnMsgFormMap>();
				    if (!StringUtil.isEmpty(s)
					    && tempValue < Integer.parseInt(client.getAlarmMinTemperature())) {
					WarnMsgFormMap warnMsgFormMap = new WarnMsgFormMap();
					warnMsgFormMap.put("warnDesc", "温度过低(" + tempValue + ")");
					warnMsgFormMap.put("warnCode", "LT001");
					warnMsgFormMap.put("warnDate", DatetimeUtil.getDate());
					warnMsgFormMap.put("equipmentId", client.getEquipmentId());
					msglist.add(warnMsgFormMap);
				    }
				    if (!StringUtil.isEmpty(s)
					    && tempValue > Integer.parseInt(client.getAlarmMaxTemperature())) {
					WarnMsgFormMap warnMsgFormMap = new WarnMsgFormMap();
					warnMsgFormMap.put("warnDesc", "温度过高(" + tempValue + ")");
					warnMsgFormMap.put("warnCode", "UT001");
					warnMsgFormMap.put("warnDate", DatetimeUtil.getDate());
					warnMsgFormMap.put("equipmentId", client.getEquipmentId());
					msglist.add(warnMsgFormMap);
				    }
				    if (!StringUtil.isEmpty(s)
					    && humValue < Integer.parseInt(client.getAlarmMinHumidity())) {
					WarnMsgFormMap warnMsgFormMap = new WarnMsgFormMap();
					warnMsgFormMap.put("warnDesc", "湿度过低(" + humValue + ")");
					warnMsgFormMap.put("warnCode", "UW001");
					warnMsgFormMap.put("warnDate", DatetimeUtil.getDate());
					warnMsgFormMap.put("equipmentId", client.getEquipmentId());
					msglist.add(warnMsgFormMap);
				    }
				    if (!StringUtil.isEmpty(s)
					    && humValue > Integer.parseInt(client.getAlarmMaxHumidity())) {
					WarnMsgFormMap warnMsgFormMap = new WarnMsgFormMap();
					warnMsgFormMap.put("warnDesc", "湿度过高(" + humValue + ")");
					warnMsgFormMap.put("warnCode", "UW001");
					warnMsgFormMap.put("warnDate", DatetimeUtil.getDate());
					warnMsgFormMap.put("equipmentId", client.getEquipmentId());
					msglist.add(warnMsgFormMap);
				    }
				    if (msglist.size() != 0) {
					warnMsgMapper.batchSave(msglist);
				    }
				    System.out.println(client.getEquipmentName() + " " + tempValue + " " + humValue);
				}
			    } catch (Exception e) {
				e.printStackTrace();
				continue;
			    }
			}
		    }
		    for (SocketClient socketClient : socketlist) {
			System.out.println(socketClient.getEquipmentName() + "   " + socketClient.getStatus());
		    }
		}
	    }, 1000, 60000);
	}
    }

}