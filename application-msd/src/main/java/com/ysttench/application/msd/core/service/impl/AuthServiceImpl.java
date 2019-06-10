package com.ysttench.application.msd.core.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.msd.core.rdto.ResponseDto;
import com.ysttench.application.msd.core.service.AuthService;
import com.ysttench.application.msd.settings.kernel.entity.ApiCabFormMap;
import com.ysttench.application.msd.settings.kernel.entity.ApiMsdQDFormMap;
import com.ysttench.application.msd.settings.kernel.mapper.ApiCabMapper;
import com.ysttench.application.msd.settings.kernel.mapper.ApiMsdCVMapper;
import com.ysttench.application.msd.settings.kernel.mapper.ApiMsdQDMapper;
import com.ysttench.application.msd.settings.web.controller.socket.TcpClient;
import com.ysttench.application.msd.settings.web.rdto.util.Modbus;

/**
 * 接口实现
 * 
 * @author Howard
 * 
 */
@WebService(targetNamespace = "http://service.core.msd.application.ysttench.com/", endpointInterface = "com.ysttench.application.msd.core.service.AuthService")
public class AuthServiceImpl implements AuthService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Inject
    ApiMsdQDMapper apiMsdQDMapper;
    @Inject
    ApiMsdCVMapper apiMsdCVMapper;
    @Inject
    ApiCabMapper apiCabMapper;

    @Override
    public String getBrightElement() {
	ResponseDto responseDto = new ResponseDto();
	try {
	    ApiMsdQDFormMap map = new ApiMsdQDFormMap();
	    map.put("requestStatus", "1");
	    List<ApiMsdQDFormMap> list = apiMsdQDMapper.findBrightElement(map);
	    if (list.size() != 0) {
		responseDto.setErrcode("0");
		responseDto.setErrmsg("获取数据成功");
		responseDto.setResponseObject(list);
	    } else {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("暂无数据");
	    }
	} catch (Exception e) {
	    logger.error("获取数据异常");
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("获取数据异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String checkBrightElement(String id) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    ApiMsdQDFormMap map = new ApiMsdQDFormMap();
	    map.put("id", id);
	    map.put("requestStatus", "2");
	    apiMsdQDMapper.editRequestStatus(map);
	    map = apiMsdQDMapper.findModbusMsg(map).get(0);
	    // apiMsdCVMapper.insertCv()
	    Modbus modbus = new Modbus(map.get("ip").toString(), Integer.parseInt(map.get("port").toString()));
	    modbus.writeCoil(Integer.parseInt(map.get("firstPoint").toString()), false);
	    modbus.writeCoil(Integer.parseInt(map.get("secondPoint").toString()), false);
	    modbus.writeCoil(Integer.parseInt(map.get("thirdPoint").toString()), false);
	    responseDto.setErrcode("0");
	    responseDto.setErrmsg("提交数据成功");
	} catch (Exception e) {
	    logger.error("提交数据异常");
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("提交数据异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String getElementList(String type) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    ApiMsdQDFormMap apiMsdQDFormMap = new ApiMsdQDFormMap();
	    apiMsdQDFormMap.put("arkType", type);
	    List<ApiMsdQDFormMap> list = apiMsdQDMapper.findMSDQDPage(apiMsdQDFormMap);
	    if (list.size() != 0) {
		responseDto.setErrcode("0");
		responseDto.setErrmsg("获取成功");
		responseDto.setResponseObject(list);
	    } else {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("暂无数据");
	    }
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("获取异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String getElement(String msdNum) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    List<ApiMsdQDFormMap> list = apiMsdQDMapper.findByAttribute("materNum", msdNum, ApiMsdQDFormMap.class);
	    if (list.size() != 0) {
		ApiMsdQDFormMap map = list.get(0);
		if (Integer.parseInt(map.getStr("resiLife")) <= 0) {
		    responseDto.setErrcode("1");
		    responseDto.setErrmsg("该元件工厂寿命小于0");
		} else if ("1".equals(map.get("requestStatus").toString())) {
		    responseDto.setErrcode("1");
		    responseDto.setErrmsg("该元件已被用户请求获取");
		} else if ("2".equals(map.get("requestStatus").toString())) {
		    responseDto.setErrcode("1");
		    responseDto.setErrmsg("该元件已被用户取用");
		} else {
		    ApiCabFormMap cmap = apiCabMapper
			    .findByAttribute("id", map.get("arkCode").toString(), ApiCabFormMap.class).get(0);
		    Modbus modbus = new Modbus(cmap.get("ip").toString(),
			    Integer.parseInt(cmap.get("port").toString()));
		    modbus.writeCoil(Integer.parseInt(cmap.get("point").toString()), true);
		    ApiCabFormMap cabmap = apiCabMapper.findbyFrist("id", cmap.get("upCab").toString(),
			    ApiCabFormMap.class);
		    Modbus ledmodbus = new Modbus(cabmap.get("ip").toString(),
			    Integer.parseInt(cabmap.get("port").toString()));
		    ledmodbus.writeCoil(Integer.parseInt(cabmap.get("point").toString()), true);
		    TcpClient client = new  TcpClient();
		    client.sendTcpMessage(cabmap.get("ledIp").toString(), Integer.parseInt(cabmap.get("ledPort").toString()),cmap.get("arkCode").toString());
		    responseDto.setErrcode("0");
		    responseDto.setErrmsg("请求成功");
		    responseDto.setResponseObject(cmap);
		}
	    } else {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("该元件未存放");
	    }
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("获取异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String getqElement(String msdNum, String qpassword) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    List<ApiMsdQDFormMap> list = apiMsdQDMapper.findByAttribute("materNum", msdNum, ApiMsdQDFormMap.class);
	    if (list.size() != 0 && !StringUtil.isEmpty(qpassword)) {
		ApiMsdQDFormMap map = list.get(0);
		ApiCabFormMap cmap = apiCabMapper
			.findByAttribute("id", map.get("arkCode").toString(), ApiCabFormMap.class).get(0);
		Modbus modbus = new Modbus(cmap.get("ip").toString(), Integer.parseInt(cmap.get("port").toString()));
		modbus.writeCoil(Integer.parseInt(cmap.get("point").toString()), true);
		ApiCabFormMap cabmap = apiCabMapper.findbyFrist("id", cmap.get("upCab").toString(),
			ApiCabFormMap.class);
		Modbus ledmodbus = new Modbus(cabmap.get("ip").toString(),
			Integer.parseInt(cabmap.get("port").toString()));
		ledmodbus.writeCoil(Integer.parseInt(cabmap.get("point").toString()), true);
		TcpClient client = new  TcpClient();
		    client.sendTcpMessage(cabmap.get("ledIp").toString(), Integer.parseInt(cabmap.get("ledPort").toString()),cmap.get("arkCode").toString());
		responseDto.setErrcode("0");
		responseDto.setErrmsg("请求成功");
		responseDto.setResponseObject(cmap);
	    }
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("请求异常");
	}
	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String saveElement(String msdNum, String arkCode) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    ApiMsdQDFormMap map = apiMsdCVMapper.findByAttribute("materNum", msdNum, ApiMsdQDFormMap.class).get(0);
	    ApiCabFormMap cmap = apiCabMapper.findByAttribute("arkCode", arkCode, ApiCabFormMap.class).get(0);
	    map.put("arkCode", cmap.get("id").toString());
	    cmap.put("status", "1");
	    apiCabMapper.editEntity(cmap);
	    apiMsdQDMapper.editEntity(map);
	    Modbus modbus = new Modbus(cmap.get("ip").toString(), Integer.parseInt(cmap.get("port").toString()));
	    modbus.writeCoil(Integer.parseInt(cmap.get("point").toString()), true);
	    ApiCabFormMap cabmap = apiCabMapper.findbyFrist("id", cmap.get("upCab").toString(), ApiCabFormMap.class);
	    Modbus ledmodbus = new Modbus(cabmap.get("ip").toString(), Integer.parseInt(cabmap.get("port").toString()));
	    ledmodbus.writeCoil(Integer.parseInt(cabmap.get("point").toString()), true);
	    TcpClient client = new  TcpClient();
	    client.sendTcpMessage(cabmap.get("ledIp").toString(), Integer.parseInt(cabmap.get("ledPort").toString()),cmap.get("arkCode").toString());
	    responseDto.setErrcode("0");
	    responseDto.setErrmsg("存放成功");
	    responseDto.setResponseObject(cmap);
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("请求异常");
	}

	return JsonUtils.bean2json(responseDto);
    }

    @Override
    public String colseElement(String ip, String port, String point, String flag, String materNum) {
	ResponseDto responseDto = new ResponseDto();
	boolean a = true;
	try {
	    if ("1".equals(flag)) {
		a = false;
	    }
	    Modbus modbus = new Modbus(ip, Integer.parseInt(port));
	    modbus.writeCoil(Integer.parseInt(point), a);
	    ApiMsdQDFormMap map = apiMsdQDMapper.findByAttribute("materNum", materNum, ApiMsdQDFormMap.class).get(0);
	    ApiCabFormMap cmap = apiCabMapper.findByAttribute("id", map.get("arkCode").toString(), ApiCabFormMap.class)
		    .get(0);
	    ApiCabFormMap cabmap = apiCabMapper.findbyFrist("id", cmap.get("upCab").toString(), ApiCabFormMap.class);
	    Modbus ledmodbus = new Modbus(cabmap.get("ip").toString(), Integer.parseInt(cabmap.get("port").toString()));
	    ledmodbus.writeCoil(Integer.parseInt(cabmap.get("point").toString()), a);
	    TcpClient client = new  TcpClient();
	    client.sendTcpMessage(cabmap.get("ledIp").toString(), Integer.parseInt(cabmap.get("ledPort").toString()),cmap.get("arkCode").toString());
	    cmap.put("status", "0");
	    ApiCabFormMap camap = apiCabMapper.findbyFrist("arkName", "手推车", ApiCabFormMap.class);
	    map.put("arkCode", camap.get("id").toString());
	    map.put("requestStatus", "0");
	    apiMsdQDMapper.editEntity(map);
	    apiCabMapper.editEntity(cmap);
	    responseDto.setErrcode("0");
	    responseDto.setErrmsg("请求成功");
	} catch (Exception e) {
	    responseDto.setErrcode("0");
	    responseDto.setErrmsg("请求异常");
	}
	return JsonUtils.bean2json(responseDto);
    }
}