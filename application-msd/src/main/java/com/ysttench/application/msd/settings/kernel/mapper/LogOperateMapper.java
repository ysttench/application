package com.ysttench.application.msd.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.msd.settings.kernel.entity.LogOperateFormMap;

public interface LogOperateMapper extends BaseMapper{
    public List<LogOperateFormMap> findByPage(LogOperateFormMap logOperateFormMap);

	public void addEntity(LogOperateFormMap logForm);
}
