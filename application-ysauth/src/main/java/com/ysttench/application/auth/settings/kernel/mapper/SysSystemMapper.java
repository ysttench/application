package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.SysSystemForMap;

public interface SysSystemMapper {

	List<SysSystemForMap> findSystem(SysSystemForMap sysSystemForMap);


	void editsystem(SysSystemForMap sysSystemForMap);



}
