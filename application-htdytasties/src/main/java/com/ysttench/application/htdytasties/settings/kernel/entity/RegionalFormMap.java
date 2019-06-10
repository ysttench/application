package com.ysttench.application.htdytasties.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 区域设置实体表
 * @author Howard
 *
 */
@TableSeg(tableName = "regional", id = "ID")
public class RegionalFormMap extends FormMap<String, Object> {
    private static final long serialVersionUID = 1L;

}