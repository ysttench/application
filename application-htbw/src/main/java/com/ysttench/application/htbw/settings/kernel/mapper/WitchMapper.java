package com.ysttench.application.htbw.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.settings.kernel.entity.WitchFormMap;

public interface WitchMapper extends BaseMapper {

    List<WitchFormMap> findWitchByPage(WitchFormMap formMap);
    
}

