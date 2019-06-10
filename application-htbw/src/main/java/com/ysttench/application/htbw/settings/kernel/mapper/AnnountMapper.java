package com.ysttench.application.htbw.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.settings.kernel.entity.AnnountFormMap;

public interface AnnountMapper extends BaseMapper {

    List<AnnountFormMap> findMsg(AnnountFormMap map);}