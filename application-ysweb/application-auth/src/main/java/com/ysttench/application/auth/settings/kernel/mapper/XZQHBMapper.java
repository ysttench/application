package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.XZQHBFormMap;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;

public interface XZQHBMapper  {
    public List<XZQHBFormMap> findCityByProvince(String provinceId);
    public List<XZQHBFormMap> findDistrictByCity(String CityId);
	public void addEntity(XZQHBFormMap xzqhbFormMap);
	public List<?> findByPage(XZQHBFormMap xzqhbFormMap);
	public List<XZQHBFormMap> findByWhere(XZQHBFormMap xZQHBFormMap);
	public void deleteByAttribute(XZQHBFormMap xZQHBFormMap);
	public void editEntity(XZQHBFormMap xzqhbFormMap);
	public List<XZQHBFormMap> findByNames(XZQHBFormMap xzqhbFormMap);
	public XZQHBFormMap findbyFrist(XZQHBFormMap xzqhbFormMap);
}
