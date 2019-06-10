package com.ysttench.application.htbw.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.settings.kernel.entity.ModbusPointFormMap;

public interface ModbusPointMapper extends BaseMapper {

    List<ModbusPointFormMap> findDetail(ModbusPointFormMap formMap);

    List<ModbusPointFormMap> findMpByPage(ModbusPointFormMap formMap);}