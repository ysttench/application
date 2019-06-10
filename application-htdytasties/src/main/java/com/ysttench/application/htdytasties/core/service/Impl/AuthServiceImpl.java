package com.ysttench.application.htdytasties.core.service.Impl;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.htdytasties.core.rdto.ResponseDto;
import com.ysttench.application.htdytasties.core.service.AuthService;
import com.ysttench.application.htdytasties.settings.kernel.entity.EquipmentFormMap;
import com.ysttench.application.htdytasties.settings.kernel.mapper.EquipmentMapper;


/**
 * 接口实现
 * 
 * @author Howard
 * 
 */
@WebService(targetNamespace = "http://service.core.hdytasties.application.ysttench.com/", endpointInterface = "com.ysttench.application.hdytasties.core.service.AuthService")
public class AuthServiceImpl implements AuthService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Inject
	EquipmentMapper equipmentMapper;

    @Override
    public String getEquipMsg() {
	ResponseDto responseDto = new ResponseDto();
	try {
	   EquipmentFormMap equipmentFormMap = new EquipmentFormMap();
	   List<EquipmentFormMap> list = equipmentMapper.findEquip(equipmentFormMap);
		   if (list.size() != 0) {
			responseDto.setErrcode("0");
			responseDto.setErrmsg("获取数据成功");
			responseDto.setResponseObject(list);
		}else {
			responseDto.setErrcode("1");
			responseDto.setErrmsg("暂无数据");
		}
	} catch (Exception e) {
	    logger.error("获取数据异常");
		responseDto.setErrcode("1");
		responseDto.setErrmsg("获取数据异常");
	}
	return JsonUtils.bean2json(responseDto);
    }}