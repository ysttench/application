package com.ysttench.application.htbw.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.settings.kernel.entity.SysRoleFormMap;

public interface SysRoleMapper extends BaseMapper{

    List<SysRoleFormMap> findRolePage(SysRoleFormMap sysRoleFormMap);

    List<SysRoleFormMap> seletUserRole(SysRoleFormMap sysRoleFormMap);

    List<SysRoleFormMap> count(SysRoleFormMap sysRoleFormMap);}
