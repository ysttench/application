package com.ysttench.application.msd.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.msd.settings.kernel.entity.ApiMsdCVFormMap;

public interface ApiMsdCVMapper extends BaseMapper{

	List<ApiMsdCVFormMap> findMSDCVPage(ApiMsdCVFormMap apiMsdCVFormMap);
}
