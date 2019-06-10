package com.ysttench.application.center.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.center.settings.kernel.entity.ApiUserFormMap;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;


public interface ApiUserMapper extends BaseMapper {

	List<ApiUserFormMap> findUserPage(ApiUserFormMap apiFormMap);

	List<ApiUserFormMap> findDetailUser(ApiUserFormMap apiUserFormMap);

	List<ApiUserFormMap> count(ApiUserFormMap apiUserFormMap);

}
