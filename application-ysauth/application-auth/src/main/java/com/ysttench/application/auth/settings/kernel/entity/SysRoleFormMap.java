package com.ysttench.application.auth.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 实体表
 */
@TableSeg(tableName = "SYS_ROLE", id = "ROLE_ID")
public class SysRoleFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
