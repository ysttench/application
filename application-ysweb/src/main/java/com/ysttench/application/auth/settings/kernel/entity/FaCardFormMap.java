package com.ysttench.application.auth.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 资产卡片实体表
 */
@TableSeg(tableName = "T_FA_CARD", id = "FAssetID")
public class FaCardFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
