package com.ysttench.application.msd.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.msd.settings.kernel.entity.SysMenuFormMap;
import com.ysttench.application.msd.settings.kernel.entity.SysRightConfigFormMap;

public interface SysMenuMapper  extends BaseMapper{
    public List<SysMenuFormMap> findChildlists(SysMenuFormMap sysMenuFormMap);

    public List<SysMenuFormMap> findRoleMenu(SysMenuFormMap sysMenuFormMap);

    public void updateSortOrder(List<SysMenuFormMap> sysMenuFormMap);

	public List<SysMenuFormMap> findByNames(SysMenuFormMap sysMenuFormMap);

	public List<SysMenuFormMap> findByWhere(SysMenuFormMap sysMenuFormMap);


	public void addEntity(SysMenuFormMap sysMenuFormMap);

	public void editEntity(SysMenuFormMap sysMenuFormMap);

	public void deleteByAttribute(String id);

	public void deleteByAttribute(SysMenuFormMap sysMenuFormMap);

	public List<SysMenuFormMap> finmenuInfo(SysMenuFormMap sysMenuFormMap);

	public List<SysMenuFormMap> findByName(SysMenuFormMap sysMenuFormMap);

	public List<SysMenuFormMap> count(SysMenuFormMap sysMenuFormMap);

	public void deleteRightConfig(SysRightConfigFormMap sysRightConfigFormMap);

	public void batchSave(SysRightConfigFormMap sysRightConfigFormMap2);


}
