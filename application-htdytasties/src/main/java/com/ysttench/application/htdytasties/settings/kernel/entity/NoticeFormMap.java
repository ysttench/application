package com.ysttench.application.htdytasties.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 用户操作记录实体表
 * @author Howard
 *
 */
@TableSeg(tableName = "notice", id = "ID")
public class NoticeFormMap extends FormMap<String, Object> {
    private static final long serialVersionUID = 1L;

}