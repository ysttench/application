package com.ysttench.application.htbw.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.settings.kernel.entity.LogRecordFormMap;

public interface LogRecordMapper extends BaseMapper {

    List<LogRecordFormMap> findMsg(LogRecordFormMap map);}