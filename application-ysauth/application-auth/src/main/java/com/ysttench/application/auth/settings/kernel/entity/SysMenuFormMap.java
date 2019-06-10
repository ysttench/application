package com.ysttench.application.auth.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 菜单实体表
 */
@TableSeg(tableName = "SYS_MENU", id = "MENU_ID")
public class SysMenuFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
