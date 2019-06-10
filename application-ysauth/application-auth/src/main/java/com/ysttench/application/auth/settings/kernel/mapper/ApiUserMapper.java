package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.ApiUserFormMap;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;


public interface ApiUserMapper  {
	
    public void insertApiUser(ApiUserFormMap apiUserFormMap);
    public void editApiUSer(ApiUserFormMap apiUserFormMap);
    public List<ApiUserFormMap> findUserPage(ApiUserFormMap apiUserFormMap);
    public void updateByIds(Long[] list);
    public List<ApiUserFormMap> findDetailUser(ApiUserFormMap apiUserFormMap);
    public List<ApiUserFormMap> findBM(ApiUserFormMap apiUserFormMap);
    public List<ApiUserFormMap> count(ApiUserFormMap apiUserFormMap);
    public String getCount(ApiUserFormMap apiUserFormMap);
	public List<ApiUserFormMap> findByNames(ApiUserFormMap param);
	public List<ApiUserFormMap> findByWhere(ApiUserFormMap apiUserFormMap);
	public void batchSave(List<ApiUserFormMap> list);
	public void editEntity(ApiUserFormMap apiUserFormMap);
	public ApiUserFormMap findbyFrist(String string, String userName, Class<ApiUserFormMap> class1);
	public List<ApiUserFormMap> findByAttribute(String columnName, String columnValue, Class<ApiUserFormMap> class1);
}
