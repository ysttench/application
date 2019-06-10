package com.ysttench.application.htdytasties.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htdytasties.settings.kernel.entity.SysRoleFormMap;
/**
 * 
 * @author Howard
 *
 */
public interface SysRoleMapper extends BaseMapper {
    /**
     * 分页查询
     * @param sysRoleFormMap
     * @return
     */
    public List<SysRoleFormMap> findRolePage(SysRoleFormMap sysRoleFormMap);
    /**
     * 唯一判断 
     * @param sysRoleFormMap
     * @return
     */
    public List<SysRoleFormMap> count(SysRoleFormMap sysRoleFormMap);
    /**
     * 获取用户对应角色
     * @param sysRoleFormMap
     * @return
     */
    public List<SysRoleFormMap> seletUserRole(SysRoleFormMap sysRoleFormMap);
}
