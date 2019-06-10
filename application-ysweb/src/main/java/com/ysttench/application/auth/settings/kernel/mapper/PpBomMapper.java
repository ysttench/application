package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.PpBomFormMap;

public interface PpBomMapper{

	List<PpBomFormMap> selectBom(PpBomFormMap ppBomFormMap);

	List<PpBomFormMap> findDetailBom(PpBomFormMap ppBomFormMap);

	List<PpBomFormMap> getorg(PpBomFormMap ppBomFormMap);



   
}
