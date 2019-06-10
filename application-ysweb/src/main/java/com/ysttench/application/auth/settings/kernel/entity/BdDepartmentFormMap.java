package com.ysttench.application.auth.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 部门实体表
 */
@TableSeg(tableName = "T_BD_DEPARTMENT", id = "FDEPTID")
public class BdDepartmentFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
