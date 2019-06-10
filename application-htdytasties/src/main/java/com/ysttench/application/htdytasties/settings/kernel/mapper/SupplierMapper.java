package com.ysttench.application.htdytasties.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.htdytasties.settings.kernel.entity.SupplierFormMap;

/**
 * 
 * @author Howard
 *
 */
public interface SupplierMapper extends BaseMapper {


    List<SupplierFormMap> count(SupplierFormMap supplierFormMap);
}
