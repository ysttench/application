package com.ysttench.application.htdytasties.settings.web.listener;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.ysttench.application.common.modbus4j.ModbusFactory;
import com.ysttench.application.common.modbus4j.ModbusMaster;
import com.ysttench.application.common.modbus4j.exception.ModbusInitException;
import com.ysttench.application.common.modbus4j.ip.IpParameters;
import com.ysttench.application.common.server.ConfigUtils;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;

/**
 * 
 * @author Howard
 */
@Repository
public class InitMapperListener implements ServletContextListener {
    static String mysql = "mysql";
    static String oracle = "oracle";
    static String f = "?";
    private static Log logger = LogFactory.getLog(InitMapperListener.class);

    @Inject
    private BaseMapper baseMapper;

    /** 从 application.properties 中读取配置 */
    @Value("${spring.datasource.url}")
    private String url = "";

    @Value("${spring.datasource.username}")
    private String username = "";

    @Value("${spring.datasource.dialect}")
    private String dialect = "";

    @Value("${spring.datasource.mybatis.entity}")
    private String entity = "";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
	logger.info("配置信息初始化Start!!!");
	if (mysql.equalsIgnoreCase(dialect)) {
	    String db = url.substring(url.lastIndexOf("/") + 1);
	    if (db.indexOf(f) > -1) {
		db = db.substring(0, db.indexOf("?"));
	    }
	    ConfigUtils.initTableFieldForMysql(baseMapper, entity, db);
	    //modbus();
	} else if (oracle.equalsIgnoreCase(dialect)) {
	    ConfigUtils.initTableFieldForOracle(baseMapper, entity, username);
	}
	logger.info("配置信息初始化End!!!");
    }

/* private void modbus() {
List<HashMap> list = new ArrayList<HashMap>();
List<ModbusMaster> mdlist = new ArrayList<ModbusMaster>();
Map<String, String> map = new HashMap<String, String>();
map.put("ip", "183.249.229.248");
map.put("port", "55002");
map.put("name", "机器1");
Map<String, String> map2 = new HashMap<String, String>();
map2.put("ip", "183.249.229.248");
map2.put("port", "55002");
map2.put("name", "机器2");
list.add((HashMap) map);
list.add((HashMap) map2);
for (int i = 0; i < list.size(); i++) {
    ModbusMaster xxxx = setmodbusConnent(map.get("ip").toString(), map.get("port").toString());
    xxxx.setName(list.get(i).get("name").toString());
    mdlist.add(xxxx);
}
	Timer timer = null;

	if (timer == null) {
	    timer = new Timer();
	    timer.schedule(new TimerTask() {
		@Override
		public void run() {
		    for (ModbusMaster modbusMaster : mdlist) {
			
		    ModbusReq.readInputRegisters(modbusMaster, new OnRequestBack<short[]>() {
			@Override
			public void onSuccess(short[] shorts) {

			    System.out.println(modbusMaster.getName()+":二氧化碳:" + shorts[0] + "  " + "温度:" + shorts[2] / 100.0 + " 时间"
				    + DatetimeUtil.fromDateH());
			}

			@Override
			public void onFailed(String msg) {
			}
		    }, 1, 0, 5);
		    }
		}
	    }, 1000, 3000);
	}

    }
*/
    public static ModbusMaster setmodbusConnent(String eqIP, String port) {
	// 连接modbus
	ModbusFactory mModbusFactory = new ModbusFactory();
	IpParameters params = new IpParameters();
	params.setHost(eqIP);
	params.setPort(Integer.parseInt(port));
	params.setEncapsulated(false);
	ModbusMaster mModbus = mModbusFactory.createTcpMaster(params, true);
	mModbus.setRetries(4);
	mModbus.setTimeout(2000);
	mModbus.setRetries(0);
	try {
	    mModbus.init();
	} catch (ModbusInitException e) {
	    mModbus.destroy();
	}
	return mModbus;
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
	logger.info("配置信息清除!!!");
    }

}