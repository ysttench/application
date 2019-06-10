package com.ysttench.application.center.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.jws.WebService;

import com.ysttench.application.center.core.rdto.ResponseDto;
import com.ysttench.application.center.core.service.AuthService;
import com.ysttench.application.center.settings.kernel.entity.ApiUserFormMap;
import com.ysttench.application.center.settings.kernel.mapper.ApiUserMapper;
import com.ysttench.application.common.util.JsonUtils;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.common.util.encrypt.Encrypt;

/**
 * 接口实现
 * 
 * @author Howard
 * 
 */
@WebService(targetNamespace = "http://service.core.center.application.ysttench.com/", endpointInterface = "com.ysttench.application.center.core.service.AuthService")
public class AuthServiceImpl implements AuthService {
    @Inject
    private ApiUserMapper apiUserMapper;
 // 密码加密
    private Encrypt encrypt = new Encrypt();
    
    @Override
    public String register(String companyName, String email, String mobile, String macAddress) {
	ResponseDto responseDto = new ResponseDto();
	try {
	    if (StringUtil.isNotEmpty(companyName) && StringUtil.isNotEmpty(email) && StringUtil.isNotEmpty(mobile)
		    && StringUtil.isNotEmpty(macAddress)) {
		ApiUserFormMap map = new ApiUserFormMap();
		map.put("companyName", companyName);
		map.put("email", email);
		map.put("mobile", mobile);
		map.put("macAddress", macAddress);
		apiUserMapper.addEntity(map);
		responseDto.setErrcode("0");
                responseDto.setErrmsg("注册成功");
                String flag =encrypt.encoder(macAddress+","+getDate());
                responseDto.setResponseObject(flag);
	    } else {
		responseDto.setErrcode("1");
		responseDto.setErrmsg("注册失败,信息不全");
	    }
	} catch (Exception e) {
	    responseDto.setErrcode("1");
	    responseDto.setErrmsg("注册失败");
	}
	return JsonUtils.bean2json(responseDto);
    }

    public String getDate() {
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar c = Calendar.getInstance();
	c.setTime(new Date());
        c.add(Calendar.YEAR, 1);
        Date y = c.getTime();
	return format.format(y);
    }
}