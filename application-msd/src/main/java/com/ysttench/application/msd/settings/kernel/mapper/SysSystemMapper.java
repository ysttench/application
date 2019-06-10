package com.ysttench.application.msd.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.msd.settings.kernel.entity.SysSystemForMap;

public interface SysSystemMapper extends BaseMapper{

	List<SysSystemForMap> findSystem(SysSystemForMap sysSystemForMap);


	void editsystem(SysSystemForMap sysSystemForMap);



}
