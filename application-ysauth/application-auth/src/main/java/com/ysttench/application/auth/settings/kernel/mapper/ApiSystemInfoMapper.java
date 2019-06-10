package com.ysttench.application.auth.settings.kernel.mapper;


import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.ApiSystemInfoFormMap;
import com.ysttench.application.auth.settings.kernel.entity.ApiUserFormMap;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;


public interface ApiSystemInfoMapper  {

    List<ApiSystemInfoFormMap> findSystemInfoPage(ApiSystemInfoFormMap apiSystemInfoFormMap);

    List<ApiUserFormMap> findDetailSystem(ApiSystemInfoFormMap apiSystemInfoFormMap);

    String getCount(ApiSystemInfoFormMap apiSystemInfoFormMap);

	void addEntity(ApiSystemInfoFormMap apiSystemInfoFormMap);

	List<ApiSystemInfoFormMap> findByNames(ApiSystemInfoFormMap paramFormMap);

	void editEntity(ApiSystemInfoFormMap apiSystemInfoFormMap);

	List<ApiSystemInfoFormMap> findByWhere(ApiSystemInfoFormMap apiSystemInfoFormMap);
}
