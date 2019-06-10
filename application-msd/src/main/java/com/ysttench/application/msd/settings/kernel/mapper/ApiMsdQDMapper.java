package com.ysttench.application.msd.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.database.ibatis.mapper.BaseMapper;
import com.ysttench.application.msd.settings.kernel.entity.ApiMsdQDFormMap;

public interface ApiMsdQDMapper extends BaseMapper {

	List<ApiMsdQDFormMap> findMSDQDPage(ApiMsdQDFormMap apiMsdQDFormMap);

	List<ApiMsdQDFormMap> findModbusMsg(ApiMsdQDFormMap apiMsdQDFormMap);

	List<ApiMsdQDFormMap> findBrightElement(ApiMsdQDFormMap map);

	void editRequestStatus(ApiMsdQDFormMap map);

}
