package com.ysttench.application.auth.settings.kernel.mapper;


import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.ApiUserRoleFormMap;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;

public interface ApiUserRoleMapper  {

	void batchSave(List<ApiUserRoleFormMap> list);

	void deleteByNames(ApiUserRoleFormMap apiUserRoleFormMap);

	void addEntity(ApiUserRoleFormMap apiUserRoleFormMap);

	List<ApiUserRoleFormMap> findByAttribute(String string, String string2, Class<ApiUserRoleFormMap> class1);

	List<ApiUserRoleFormMap> findByNames(ApiUserRoleFormMap apiUserRoleFormMap);
	
	

}
