package com.ysttench.application.htdytasties.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htdytasties.settings.kernel.entity.EquipTypeFormMap;

/**
 * 
 * @author Howard
 *
 */
public interface EquipTypeMapper extends BaseMapper {

    List<EquipTypeFormMap> count(EquipTypeFormMap equipTypeFormMap);
}
