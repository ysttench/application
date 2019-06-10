package com.ysttench.application.msd.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.msd.settings.kernel.entity.LogUserLoginFormMap;

public interface LogUserLoginMapper extends BaseMapper{
    public List<LogUserLoginFormMap> findByPage(LogUserLoginFormMap logUserLoginFormMap);

	public void addlog(LogUserLoginFormMap logUserLoginFormMap);
}
