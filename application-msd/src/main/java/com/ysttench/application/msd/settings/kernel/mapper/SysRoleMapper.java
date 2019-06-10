package com.ysttench.application.msd.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.msd.settings.kernel.entity.SysRoleFormMap;

public interface SysRoleMapper extends BaseMapper{
    public List<SysRoleFormMap> seletUserRole(SysRoleFormMap sysRoleFormMap);

    public void deleteById(SysRoleFormMap sysRoleFormMap);

    public List<SysRoleFormMap> findRolePage(SysRoleFormMap sysRoleFormMap);

    public List<SysRoleFormMap> count(SysRoleFormMap sysRoleFormMap);

	public void addEntity(SysRoleFormMap sysRoleFormMap);

	public void editEntity(SysRoleFormMap sysRoleFormMap);

	public void deleteByAttribute(SysRoleFormMap sysRoleFormMap);

	public List<SysRoleFormMap> findByWhere(SysRoleFormMap sysRoleFormMap);

	public SysRoleFormMap findbyFrist(SysRoleFormMap sysRoleFormMap);
}
