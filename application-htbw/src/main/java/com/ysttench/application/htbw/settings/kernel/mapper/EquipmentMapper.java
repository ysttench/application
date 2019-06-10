package com.ysttench.application.htbw.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.settings.kernel.entity.EquipmentFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.EquipmentMsgFormMap;
import com.ysttench.application.htbw.settings.kernel.entity.WitchFormMap;

public interface EquipmentMapper extends BaseMapper {

    List<EquipmentFormMap> findDetail(EquipmentFormMap map);

    List<EquipmentFormMap> findEquipPage(EquipmentFormMap map);

    List<EquipmentFormMap> findMsg(EquipmentFormMap map);

    List<EquipmentMsgFormMap> findJkMsg(EquipmentMsgFormMap formMap);

    List<EquipmentMsgFormMap> findChangeCurv(EquipmentMsgFormMap map);

    List<WitchFormMap> getWitchMsg(WitchFormMap witchFormMap);


    List<EquipmentFormMap> findbtemChangeCurv(EquipmentFormMap map);

    List<EquipmentFormMap> findbhumChangeCurv(EquipmentFormMap map);
}