package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.LgDForMap;

public interface LgDMapper {

	List<LgDForMap> findAllPage(LgDForMap lgDForMap);

	List<LgDForMap> count(LgDForMap lgDForMap);
	List<LgDForMap> count2(LgDForMap lgDForMap);

}
