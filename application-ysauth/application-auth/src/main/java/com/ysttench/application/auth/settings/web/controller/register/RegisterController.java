package com.ysttench.application.auth.settings.web.controller.register;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ysttench.application.auth.settings.kernel.entity.ApiUserFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.ApiUserMapper;
import com.ysttench.application.auth.settings.web.controller.common.ForwardConstants;
import com.ysttench.application.auth.settings.web.controller.index.BaseController;

/**
 * 应用用户通用注册
 * 
 * @author gengyong
 *
 */
@Controller
@RequestMapping("/register/")
public class RegisterController extends BaseController {
    @Inject
    private ApiUserMapper apiUserMapper;
    
    @RequestMapping(value = "add")
    public String register(Model model) {
        return ForwardConstants.REGISTER + ForwardConstants.ADD;

    }
    
    @ResponseBody
    @RequestMapping("checkField")
    public Boolean checkField(String fieldName,String value){
        ApiUserFormMap apiUserFormMap = new ApiUserFormMap();
        apiUserFormMap.put(fieldName, value);
        List<ApiUserFormMap> apiUserList = apiUserMapper.findByNames(apiUserFormMap);
        if(null!=apiUserList){
            if(apiUserList.size()>0){
                return Boolean.FALSE;
            }else{
                return Boolean.TRUE;
            }
        }else{
            return Boolean.TRUE;
        }
    }
}