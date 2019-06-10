package com.ysttench.application.htdytasties.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htdytasties.settings.kernel.entity.EquipmentFormMap;

/**
 * 
 * @author Howard
 *
 */
public interface NoticeMapper extends BaseMapper {

    List<EquipmentFormMap> findEquipPage(EquipmentFormMap equipmentFormMap);

    List<EquipmentFormMap> findEquip(EquipmentFormMap equipmentFormMap);

}
