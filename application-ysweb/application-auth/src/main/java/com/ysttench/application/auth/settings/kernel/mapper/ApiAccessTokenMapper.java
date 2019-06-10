package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.ApiAccessTokenFormMap;




public interface ApiAccessTokenMapper {
    public List<ApiAccessTokenFormMap> seletUserRole(ApiAccessTokenFormMap ApiAccessTokenFormMap);

	public void addEntity(ApiAccessTokenFormMap apiAccessTokenFormMap);

    
}
