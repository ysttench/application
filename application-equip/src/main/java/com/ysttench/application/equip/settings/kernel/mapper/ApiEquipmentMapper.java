package com.ysttench.application.equip.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.equip.settings.kernel.entity.ApiEquipmentFormMap;
import com.ysttench.application.equip.settings.kernel.entity.ApiEquipmentSpareFormMap;
import com.ysttench.application.equip.settings.kernel.entity.ApiExceptFormMap;
import com.ysttench.application.equip.settings.kernel.entity.ApiGrainFormMap;
import com.ysttench.application.equip.settings.kernel.entity.ApiMntenceFormMap;

public interface ApiEquipmentMapper{

	List<ApiEquipmentFormMap> findEquipmentPage(ApiEquipmentFormMap apiEquipmentFormMap);

	List<ApiEquipmentFormMap> findCkEquipmentPage(ApiEquipmentFormMap apiEquipmentFormMap);

	void addEntity(ApiEquipmentFormMap apiEquipmentFormMap);

	void addEntityru(ApiEquipmentSpareFormMap apiEquipmentSpareFormMap);

	void deleteEquipmentSpare(ApiEquipmentSpareFormMap apiEquipmentSpareFormMap);

	void editEquipment(ApiEquipmentFormMap apiEquipmentFormMap);

	List<ApiEquipmentFormMap> findDetailEquip(ApiEquipmentFormMap apiEquipmentFormMap);

	void editEntity(ApiEquipmentFormMap apiEquipmentFormMap);

	List<ApiExceptFormMap> findExceptByPage(ApiExceptFormMap apiExceptFormMap);

	List<ApiGrainFormMap> findgainByPage(ApiGrainFormMap apiGrainFormMap);

	List<ApiMntenceFormMap> findmntenceByPage(ApiMntenceFormMap apiMntenceFormMap);

}
