package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.BdStockFormMap;

public interface BdStockMapper{

	List<BdStockFormMap> findBdStockMsg(BdStockFormMap bdStockFormMap);

	List<BdStockFormMap> getcw(BdStockFormMap bdStockFormMap);


   
}
