package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.LogOperateFormMap;

public interface LogOperateMapper {
    public List<LogOperateFormMap> findByPage(LogOperateFormMap logOperateFormMap);

	public void addEntity(LogOperateFormMap logForm);
}
