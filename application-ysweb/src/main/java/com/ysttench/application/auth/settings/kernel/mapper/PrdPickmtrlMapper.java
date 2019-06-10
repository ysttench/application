package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.PrdPickmtrlFormMap;

public interface PrdPickmtrlMapper{

	List<PrdPickmtrlFormMap> findprdMsg(PrdPickmtrlFormMap prdPickmtrlFormMap);

	 List<PrdPickmtrlFormMap> findDetailpicMsg(PrdPickmtrlFormMap pickmtrlFormMap);

	List<PrdPickmtrlFormMap> findprdMsgByPage(PrdPickmtrlFormMap prdPickmtrlFormMap);

	List<PrdPickmtrlFormMap> findDetailprdMsg(PrdPickmtrlFormMap prdPickmtrlFormMap);


   
}
