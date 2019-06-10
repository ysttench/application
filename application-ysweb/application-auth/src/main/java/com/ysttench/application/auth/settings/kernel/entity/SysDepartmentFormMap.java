package com.ysttench.application.auth.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 部门实体表
 */
@TableSeg(tableName = "SYS_DEPARTMENT", id = "ID")
public class SysDepartmentFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
