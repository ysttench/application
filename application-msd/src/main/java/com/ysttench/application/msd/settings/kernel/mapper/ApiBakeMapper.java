package com.ysttench.application.msd.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.msd.settings.kernel.entity.ApiBakeFormMap;

public interface ApiBakeMapper extends BaseMapper {

	List<ApiBakeFormMap> findbakePage(ApiBakeFormMap apiBakeFormMap);}
