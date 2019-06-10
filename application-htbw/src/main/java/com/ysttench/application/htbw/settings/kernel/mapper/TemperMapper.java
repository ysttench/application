package com.ysttench.application.htbw.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.settings.kernel.entity.TemperFormMap;

public interface TemperMapper extends BaseMapper {

    List<TemperFormMap> findTemByPage(TemperFormMap formMap);}