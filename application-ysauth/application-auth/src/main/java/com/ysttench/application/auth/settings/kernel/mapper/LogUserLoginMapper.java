package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.LogUserLoginFormMap;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;

public interface LogUserLoginMapper {
    public List<LogUserLoginFormMap> findByPage(LogUserLoginFormMap logUserLoginFormMap);

	public void addlog(LogUserLoginFormMap logUserLoginFormMap);
}
