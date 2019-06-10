package com.ysttench.application.auth.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 员工实体表
 */
@TableSeg(tableName = "T_BD_STAFF", id = "FSTAFFID")
public class BdStaffFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
