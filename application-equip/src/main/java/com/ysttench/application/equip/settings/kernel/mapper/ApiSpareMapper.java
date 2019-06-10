package com.ysttench.application.equip.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.equip.settings.kernel.entity.ApiEquipmentSpareFormMap;
import com.ysttench.application.equip.settings.kernel.entity.ApiSpareFormMap;
import com.ysttench.application.equip.settings.kernel.entity.SysRoleFormMap;

public interface ApiSpareMapper{

	List<ApiSpareFormMap> findSparePage(ApiSpareFormMap apiSpareFormMap);

	void addEntity(ApiSpareFormMap apiSpareFormMap);

	void editEntity(ApiSpareFormMap apiSpareFormMap);

	List<ApiSpareFormMap> findDetailSpare(ApiSpareFormMap apiSpareFormMap);

	void editEntity1(ApiSpareFormMap apiSpareFormMap);

	List<ApiSpareFormMap> findByWhere(ApiSpareFormMap apiSpareFormMap);

	List<ApiSpareFormMap> seletEquipSpare(ApiSpareFormMap apiSpareFormMap);

	List<ApiSpareFormMap> findSparePage2(ApiSpareFormMap apiSpareFormMap);

	void editEquimentSpare(ApiEquipmentSpareFormMap apiEquipmentSpareFormMap);

	List<ApiEquipmentSpareFormMap> findDetailEquipmentSpare(ApiEquipmentSpareFormMap apiEquipmentSpareFormMa);

	void editNum(ApiEquipmentSpareFormMap apiEquipmentSpareFormMap);

}
