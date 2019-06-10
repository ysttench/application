package com.ysttench.application.auth.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * user实体表
 */
@TableSeg(tableName = "API_USER_ROLE", id = "USER_ID")
public class ApiUserRoleFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
