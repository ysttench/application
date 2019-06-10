package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.ApiLogUserLoginFormMap;
import com.ysttench.application.auth.settings.kernel.entity.LogUserLoginFormMap;

public interface ApiLogUserLoginMapper {
    public List<LogUserLoginFormMap> findByPage(LogUserLoginFormMap logUserLoginFormMap);

    public List<ApiLogUserLoginFormMap> getData(ApiLogUserLoginFormMap apiLogUserLoginFormMap);

    public List<ApiLogUserLoginFormMap> getNum(ApiLogUserLoginFormMap apiLogUserLoginFormMap);

	public void addEntity(ApiLogUserLoginFormMap apiLogUserLoginFormMap);

	public List<?> findByPage(ApiLogUserLoginFormMap logUserLoginFormMap);
}
