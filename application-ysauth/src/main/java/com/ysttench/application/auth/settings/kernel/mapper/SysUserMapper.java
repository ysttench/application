package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.SysUserFormMap;
import com.ysttench.application.auth.settings.kernel.entity.SysUserRoleFormMap;

public interface SysUserMapper{

    public List<SysUserFormMap> findUserPage(SysUserFormMap sysUserFormMap);

    public List<SysUserFormMap> count(SysUserFormMap sysUserFormMap);

    public List<SysUserFormMap> findDetailUser(SysUserFormMap sysUserFormMap);

	public void addEntity(SysUserFormMap sysUserFormMap);

	public void editEntity(SysUserFormMap sysUserFormMap);

	public void addEntityru(SysUserRoleFormMap sysUserRoleFormMap);
	public void editEn(SysUserFormMap sysUserFormMap);
	public void deleteUserRole(SysUserRoleFormMap sysUserRoleFormMap);

	public void editUser(SysUserFormMap sysUserFormMap);

	public List<SysUserFormMap> perm(SysUserFormMap userFormMap);

}
