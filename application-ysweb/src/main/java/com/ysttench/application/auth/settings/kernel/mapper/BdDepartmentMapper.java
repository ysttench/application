package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.BdDepartmentFormMap;

public interface BdDepartmentMapper{

	List<BdDepartmentFormMap> findMsg(BdDepartmentFormMap formMap);

}
