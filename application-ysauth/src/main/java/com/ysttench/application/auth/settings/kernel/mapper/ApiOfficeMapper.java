package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.ApiOfficeFormMap;

public interface ApiOfficeMapper {

	List<ApiOfficeFormMap> findOfficeByPage(ApiOfficeFormMap apiOfficeFormMap);

	void insertOffice(ApiOfficeFormMap apiOfficeFormMap);

	List<ApiOfficeFormMap> findDetailOffice(ApiOfficeFormMap apiOfficeFormMap);

	void editOffice(ApiOfficeFormMap apiOfficeFormMap);

	void deleteById(ApiOfficeFormMap apiOfficeFormMap);

	List<ApiOfficeFormMap> findsuperOffice(ApiOfficeFormMap apiOfficeFormMap);

	List<ApiOfficeFormMap> count(ApiOfficeFormMap apiOfficeFormMap);

	List<ApiOfficeFormMap> findupOffice(ApiOfficeFormMap apiOfficeFormMap);

	List<ApiOfficeFormMap> findsuperid(ApiOfficeFormMap apiOfficeFormMap);

}
