package com.ysttench.application.htdytasties.settings.kernel.entity;

import com.ysttench.application.common.annotation.TableSeg;
import com.ysttench.application.database.ibatis.entity.FormMap;

/**
 * 打印机配置实体表
 * @author Howard
 *
 */
@TableSeg(tableName = "dprint", id = "ID")
public class SysPrintForMap  extends FormMap<String, Object> {

	private static final long serialVersionUID = 1L;

}
