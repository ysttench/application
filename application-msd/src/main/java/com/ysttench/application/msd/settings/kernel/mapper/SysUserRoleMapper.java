package com.ysttench.application.msd.settings.kernel.mapper;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.msd.settings.kernel.entity.SysUserRoleFormMap;

public interface SysUserRoleMapper extends BaseMapper{

	void deleteByAttribute(SysUserRoleFormMap sysUserRoleFormMap);

}
