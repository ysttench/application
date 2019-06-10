package com.ysttench.application.htbw.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.settings.kernel.entity.SysUserFormMap;

public interface SysUserMapper extends BaseMapper{

    List<SysUserFormMap> findUserPage(SysUserFormMap userFormMap);

    List<SysUserFormMap> findDetailUser(SysUserFormMap sysUserFormMap);

    List<SysUserFormMap> count(SysUserFormMap sysUserFormMap);

    List<SysUserFormMap> seletEquipUser(SysUserFormMap formMap);}
