package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.ApiStAssetsForMap;

public interface ApiStAssetsMapper {

	List<ApiStAssetsForMap> findAllPage(ApiStAssetsForMap apiStAssetsForMap);
	List<ApiStAssetsForMap> findAllMsg(ApiStAssetsForMap apiStAssetsForMap);

	void addEntity(ApiStAssetsForMap apiStAssetsForMap);

	void delete(ApiStAssetsForMap apiStAssetsForMap);

	List<ApiStAssetsForMap> count(ApiStAssetsForMap apiStAssetsForMap);

	void edit(ApiStAssetsForMap apiStAssetsForMap);
	void editEntity(ApiStAssetsForMap apiStAssetsForMap);
	List<ApiStAssetsForMap> findAllPage1(ApiStAssetsForMap apiStAssetsForMap);

	List<ApiStAssetsForMap> findDetailMsg(ApiStAssetsForMap apiStAssetsForMap);
	List<ApiStAssetsForMap> findAllPrint(ApiStAssetsForMap apiStAssetsForMap);


}
