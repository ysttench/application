package com.ysttench.application.htbw.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htbw.settings.kernel.entity.SysMenuFormMap;

public interface SysMenuMapper extends BaseMapper {
    public List<SysMenuFormMap> findChildlists(SysMenuFormMap sysMenuFormMap);

    public List<SysMenuFormMap> findRoleMenu(SysMenuFormMap sysMenuFormMap);

    public void updateSortOrder(List<SysMenuFormMap> sysMenuFormMap);

    public List<SysMenuFormMap> count(SysMenuFormMap sysMenuFormMap);
}
