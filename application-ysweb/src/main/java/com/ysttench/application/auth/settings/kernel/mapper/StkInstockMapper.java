package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.StkInstockFormMap;

public interface StkInstockMapper{

	List<StkInstockFormMap> findStkinsStockMsg(StkInstockFormMap stkInstockFormMap);

	List<StkInstockFormMap> findDetailBdStockMsg(StkInstockFormMap stkInstockFormMap);

	List<StkInstockFormMap> findStkinsStockMsgByPage(StkInstockFormMap stkInstockFormMap);


   
}
