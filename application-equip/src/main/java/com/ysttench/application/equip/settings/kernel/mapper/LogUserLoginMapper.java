package com.ysttench.application.equip.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.equip.settings.kernel.entity.LogUserLoginFormMap;

public interface LogUserLoginMapper {
    public List<LogUserLoginFormMap> findByPage(LogUserLoginFormMap logUserLoginFormMap);

	public void addlog(LogUserLoginFormMap logUserLoginFormMap);
}
