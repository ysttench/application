package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.SysUserFormMap;
import com.ysttench.application.auth.settings.kernel.entity.SysUserRoleFormMap;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;

public interface SysUserMapper{

    public List<SysUserFormMap> findUserPage(SysUserFormMap sysUserFormMap);

    public List<SysUserFormMap> count(SysUserFormMap sysUserFormMap);

    public List<SysUserFormMap> findDetailUser(SysUserFormMap sysUserFormMap);

	public void addEntity(SysUserFormMap sysUserFormMap);

	public void editEntity(SysUserFormMap sysUserFormMap);

	public void addEntity(SysUserRoleFormMap userGroupsFormMap);

	public List<SysUserFormMap> findByNames(SysUserFormMap oneUser);

}
