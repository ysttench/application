package com.ysttench.application.htdytasties.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htdytasties.settings.kernel.entity.SysMenuFormMap;

/**
 * 
 * @author Howard
 *
 */
public interface SysMenuMapper extends BaseMapper {
    /**
     * 获取子类菜单
     * @param sysMenuFormMap
     * @return
     */
    public List<SysMenuFormMap> findChildlists(SysMenuFormMap sysMenuFormMap);
    /**
     * 获取角色对应的菜单
     * @param sysMenuFormMap
     * @return
     */
    public List<SysMenuFormMap> findRoleMenu(SysMenuFormMap sysMenuFormMap);
    /**
     * 修改菜单排序
     * @param sysMenuFormMap
     */
    public void updateSortOrder(List<SysMenuFormMap> sysMenuFormMap);
    /**
     * 判断唯一
     * @param sysMenuFormMap
     * @return
     */
    public List<SysMenuFormMap> count(SysMenuFormMap sysMenuFormMap);

}
