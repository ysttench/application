package com.ysttench.application.htbw.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.settings.kernel.entity.RegionalFormMap;

/**
 * 
 * @author Howard
 *
 */
public interface RegionalMapper extends BaseMapper {
/** 分页查询*/
    List<RegionalFormMap> findRegionalPage(RegionalFormMap regionalFormMap);
    /** 详细信息查询*/
    List<RegionalFormMap> findDetailRegional(RegionalFormMap regionalFormMap);
    /** 唯一判断*/
    List<RegionalFormMap> count(RegionalFormMap regionalFormMap);
}
