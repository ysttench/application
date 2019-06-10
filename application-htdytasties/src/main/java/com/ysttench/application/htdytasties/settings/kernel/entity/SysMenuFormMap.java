package com.ysttench.application.htdytasties.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 系统菜单实体表
 * @author Howard
 *
 */
@TableSeg(tableName = "sys_menu", id = "MENU_ID")
public class SysMenuFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
