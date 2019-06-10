package com.ysttench.application.auth.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 入库单实体表
 */
@TableSeg(tableName = "T_STK_INSTOCK", id = "FID")
public class StkInstockFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
