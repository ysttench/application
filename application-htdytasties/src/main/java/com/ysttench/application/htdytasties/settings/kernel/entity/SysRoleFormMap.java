package com.ysttench.application.htdytasties.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 系统角色实体表
 * @author Howard
 *
 */
@TableSeg(tableName = "sys_role", id = "ROLE_ID")
public class SysRoleFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
