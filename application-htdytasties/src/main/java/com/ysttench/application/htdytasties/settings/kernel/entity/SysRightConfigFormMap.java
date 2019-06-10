package com.ysttench.application.htdytasties.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 系统角色菜单关系表
 * @author Howard
 *
 */
@TableSeg(tableName = "sys_right_config", id = "ROLE_ID")
public class SysRightConfigFormMap extends FormMap<String, Object> {
    private static final long serialVersionUID = 1L;

}
