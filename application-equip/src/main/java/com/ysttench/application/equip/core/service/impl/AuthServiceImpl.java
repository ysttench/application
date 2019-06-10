package com.ysttench.application.equip.core.service.impl;

import java.util.List;
import javax.inject.Inject;
import javax.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.equip.core.rdto.EquipException;
import com.ysttench.application.equip.core.rdto.ResponseDto;
import com.ysttench.application.equip.core.service.AuthService;
import com.ysttench.application.equip.settings.kernel.entity.ApiExceptFormMap;
import com.ysttench.application.equip.settings.kernel.mapper.ApiExceptionMapper;


/**
 * 接口实现
 * 
 * @author Howard
 * 
 */
@WebService(targetNamespace = "http://service.core.equip.application.ysttench.com/", endpointInterface = "com.ysttench.application.equip.core.service.AuthService")
public class AuthServiceImpl implements AuthService {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
@Inject ApiExceptionMapper apiExceptionMapper;
	@Override
	public String exceptionMsg(List<EquipException> list) {
		ResponseDto responseDto = new ResponseDto();
		try {
			for (EquipException equipException : list) {
				ApiExceptFormMap map= new ApiExceptFormMap();
				map.put("equipmentNum", equipException.getEquipmentNum());
				map.put("exceptCode", equipException.getExceptCode());
				map.put("startTime", equipException.getStartTime());
				map.put("endTime", equipException.getEndTime());
				map.put("description", equipException.getDescription());
				map.put("dracution", equipException.getDracution());
				apiExceptionMapper.addEntity(map);
			}
			responseDto.setErrcode("0");
			responseDto.setErrmsg("同步数据成功");
		} catch (Exception e) {
			logger.error("同步数据异常");
			responseDto.setErrcode("1");
			responseDto.setErrmsg("同步数据异常");
		}
		return JsonUtils.bean2json(responseDto);
	}
	@Override
	public String exceptionMsg2(String json) {

		ResponseDto responseDto = new ResponseDto();
		try {
			JSONArray jsonArray = JSONArray.parseArray(json);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jobj = (JSONObject) jsonArray.get(i);
				ApiExceptFormMap map= new ApiExceptFormMap();
				map.put("equipmentNum", jobj.get(""));
				map.put("exceptCode", jobj.get(""));
				map.put("startTime", jobj.get(""));
				map.put("endTime", jobj.get(""));
				map.put("description", jobj.get(""));
				map.put("dracution", jobj.get(""));
				apiExceptionMapper.addEntity(map);
			}
			responseDto.setErrcode("0");
			responseDto.setErrmsg("同步异常数据成功");
		} catch (Exception e) {
			logger.error("同步数据异常");
			responseDto.setErrcode("0");
			responseDto.setErrmsg("同步数据异常");
		}
		return JsonUtils.bean2json(responseDto);
	
	}



}