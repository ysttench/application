package com.ysttench.application.htdytasties.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;
/**
 * 系统用户实体表
 * @author Howard
 *
 */
@TableSeg(tableName = "sys_user", id = "USER_ID")
public class SysUserFormMap extends FormMap<String, Object> {

    private static final long serialVersionUID = 1L;

}
