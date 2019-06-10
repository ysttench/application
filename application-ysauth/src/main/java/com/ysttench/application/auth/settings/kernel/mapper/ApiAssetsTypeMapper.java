package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.ApiAssetsTypeFormMap;

public interface ApiAssetsTypeMapper {

	List<ApiAssetsTypeFormMap> findDetailType(ApiAssetsTypeFormMap apiAssetsTypeFormMap);

	void insertAssetsType(ApiAssetsTypeFormMap apiAssetsTypeFormMap);

	List<ApiAssetsTypeFormMap> findAssetsTypeByPage(ApiAssetsTypeFormMap apiAssetsTypeFormMap);

	void editType(ApiAssetsTypeFormMap apiAssetsTypeFormMap);

	void deleteById(ApiAssetsTypeFormMap apiAssetsTypeFormMap);

	List<ApiAssetsTypeFormMap> count(ApiAssetsTypeFormMap apiAssetsTypeFormMap);

	List<ApiAssetsTypeFormMap> findType(ApiAssetsTypeFormMap apiAssetsTypeFormMap);


		


}
