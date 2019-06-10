package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.StkInventoryFormMap;

public interface StkInventoryMapper{

	List<StkInventoryFormMap> findventoryMsg(StkInventoryFormMap stkInventoryFormMap);
	List<StkInventoryFormMap> findventoryMsgByPage(StkInventoryFormMap stkInventoryFormMap);


   
}
