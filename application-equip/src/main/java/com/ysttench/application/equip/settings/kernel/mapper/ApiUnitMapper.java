package com.ysttench.application.equip.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.equip.settings.kernel.entity.ApiUnitFormMap;

public interface ApiUnitMapper {

	List<ApiUnitFormMap> findUnitByPage(ApiUnitFormMap apiUnitFormMap);

	void insertUnit(ApiUnitFormMap apiUnitFormMap);

	List<ApiUnitFormMap> count(ApiUnitFormMap apiUnitFormMap);

	List<ApiUnitFormMap> findDetailUnit(ApiUnitFormMap apiUnitFormMap);

	void editUnit(ApiUnitFormMap apiUnitFormMap);

	void deleteById(ApiUnitFormMap apiUnitFormMap);


}
