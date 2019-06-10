package com.ysttench.application.equip.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.equip.settings.kernel.entity.SysUserFormMap;
import com.ysttench.application.equip.settings.kernel.entity.SysUserRoleFormMap;

public interface SysUserMapper extends BaseMapper{

    public List<SysUserFormMap> findUserPage(SysUserFormMap sysUserFormMap);

    public List<SysUserFormMap> count(SysUserFormMap sysUserFormMap);

    public List<SysUserFormMap> findDetailUser(SysUserFormMap sysUserFormMap);

	public void addEntity(SysUserFormMap sysUserFormMap);

	public void editEntity(SysUserFormMap sysUserFormMap);

	public void addEntityru(SysUserRoleFormMap sysUserRoleFormMap);

	public List<SysUserFormMap> findByNames(SysUserFormMap oneUser);

	public void deleteUserRole(SysUserRoleFormMap sysUserRoleFormMap);

	public void editUser(SysUserFormMap sysUserFormMap);

}
