package com.ysttench.application.auth.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 即时库存实体表
 */
@TableSeg(tableName = "T_STK_INVENTORY", id = "FID")
public class StkInventoryFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
