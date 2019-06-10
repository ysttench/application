package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.FaCardFormMap;

public interface FaCardMapper{

	List<FaCardFormMap> findGDMsg(FaCardFormMap faCardFormMap);

	List<FaCardFormMap> findGDMsgByPage(FaCardFormMap faCardFormMap);

	List<FaCardFormMap> findDetailCardMsg(FaCardFormMap faCardFormMap);

	List<FaCardFormMap> findGdPdMsg(FaCardFormMap faCardFormMap);

	List<FaCardFormMap> findDetaiGdPdMsg(FaCardFormMap faCardFormMap);

	List<FaCardFormMap> findGdpd(FaCardFormMap formap);

	void edit(FaCardFormMap faformap);



   
}
