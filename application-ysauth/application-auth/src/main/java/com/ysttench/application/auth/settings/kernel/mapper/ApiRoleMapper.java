package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.ApiRoleFormMap;
import com.ysttench.application.auth.settings.kernel.entity.ApiSystemInfoFormMap;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;

public interface ApiRoleMapper {

    public List<ApiRoleFormMap> findRoleByPage(ApiRoleFormMap apiRoleFormMap);

    public List<ApiRoleFormMap> seletUserRole(ApiRoleFormMap apiRoleFormMap);

    public void setDeleteById(String id);

    public List<ApiRoleFormMap> isRoleExist(ApiRoleFormMap apiRoleFormMap);

    public List<ApiRoleFormMap> isRoleBury(ApiRoleFormMap apiRoleFormMap);

    public List<ApiRoleFormMap> findXT(ApiRoleFormMap apiRoleFormMap);

    public List<ApiRoleFormMap> findDetail(ApiRoleFormMap apiRoleFormMap);

    public List<ApiRoleFormMap> count(ApiRoleFormMap apiRoleFormMap);

	public void addEntity(ApiRoleFormMap apiRoleFormMap);

	public List<ApiRoleFormMap> findByAttribute(String string, String roleKey, Class<ApiRoleFormMap> class1);

	public void editEntity(ApiRoleFormMap apiRoleFormMap);

	public List<ApiRoleFormMap> findByWhere(ApiRoleFormMap apiRoleFormMap);

	public List<ApiSystemInfoFormMap> findByWhere(ApiSystemInfoFormMap apiSystemInfoFormMap);


}
