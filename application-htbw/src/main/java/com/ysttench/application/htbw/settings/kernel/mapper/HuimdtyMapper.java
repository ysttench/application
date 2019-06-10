package com.ysttench.application.htbw.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.settings.kernel.entity.HuimdtyFormMap;

public interface HuimdtyMapper extends BaseMapper {

    List<HuimdtyFormMap> finHumByPage(HuimdtyFormMap formMap);}