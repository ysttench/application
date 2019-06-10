package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.LogOperateFormMap;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;

public interface LogOperateMapper {
    public List<LogOperateFormMap> findByPage(LogOperateFormMap logOperateFormMap);

	public void addEntity(LogOperateFormMap logForm);
}
