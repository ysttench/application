package com.ysttench.application.center.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.center.settings.kernel.entity.LogOperateFormMap;

public interface LogOperateMapper {
    public List<LogOperateFormMap> findByPage(LogOperateFormMap logOperateFormMap);

	public void addEntity(LogOperateFormMap logForm);
}
