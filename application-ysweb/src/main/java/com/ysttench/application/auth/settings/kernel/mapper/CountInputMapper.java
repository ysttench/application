package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.CountInputFormMap;

public interface CountInputMapper{

	List<CountInputFormMap> findMsg(CountInputFormMap countInputFormMap);

	List<CountInputFormMap> findDetaiMsg(CountInputFormMap countInputFormMap);

	List<CountInputFormMap> findDetaipD(CountInputFormMap countInputFormMap);

	List<CountInputFormMap> findpd(CountInputFormMap countInputFormMap);

	void edit(CountInputFormMap countInputFormMap);

	List<CountInputFormMap> findCkMsg(CountInputFormMap countInputFormMap);

}
