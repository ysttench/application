package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.LgDForMap;
import com.ysttench.application.auth.settings.kernel.entity.LgForMap;

public interface LgMapper {

	void add(LgDForMap lgDForMap);

	void addEntity(LgDForMap lgDForMap);

	List<LgForMap> findAllPage(LgForMap lgForMap);

	List<LgForMap> findAllMsg(LgForMap lgForMap);

	List<LgForMap> findAllpyMsg(LgForMap lgForMap);

	List<LgForMap> findkMsg(LgForMap lgForMap);

	List<LgForMap> findAllpkMsg(LgForMap lgForMap);

	void edit(LgDForMap jgDForMap);
	
	List<LgDForMap> finMsg(LgDForMap jgDForMap);

	List<LgForMap> findAllMsg2(LgForMap lgForMap);

	List<LgDForMap> findAllPage1(LgDForMap lgForMap);

	List<LgDForMap> getcount(LgDForMap lgDForMap);





}
