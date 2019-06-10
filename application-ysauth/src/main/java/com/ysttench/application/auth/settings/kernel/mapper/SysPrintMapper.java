package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.HashMap;
import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.SysPrintForMap;

public interface SysPrintMapper {

	List<SysPrintForMap> findPrintPage(SysPrintForMap sysPrintForMap);

	void addEntity(SysPrintForMap sysPrintForMap);

	void deleteByAttribute(SysPrintForMap sysPrintForMap);

	SysPrintForMap findbyFrist(SysPrintForMap sysPrintForMap);

	void editEntity(SysPrintForMap sysPrintForMap);

	List<SysPrintForMap> count(SysPrintForMap sysPrintForMap);

	List<SysPrintForMap> findDetail(SysPrintForMap sysPrintForMap);



}
