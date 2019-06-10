package com.ysttench.application.htdytasties.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htdytasties.settings.kernel.entity.SysUserFormMap;
/**
 * 
 * @author Howard
 *
 */
public interface SysUserMapper extends BaseMapper{
    /**
     * 分页查询
     * @param sysUserFormMap
     * @return
     */
    public List<SysUserFormMap> findUserPage(SysUserFormMap sysUserFormMap);
    /**
     * 唯一判断
     * @param sysUserFormMap
     * @return
     */
    public List<SysUserFormMap> count(SysUserFormMap sysUserFormMap);
    /**
     * 获取用户详情
     * @param sysUserFormMap
     * @return
     */
    public List<SysUserFormMap> findDetailUser(SysUserFormMap sysUserFormMap);

}
