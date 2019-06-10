package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.BdStaffFormMap;


public interface BdStaffMapper{

	List<BdStaffFormMap> findMsg(BdStaffFormMap formMap);

}
