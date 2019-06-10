package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.ApiSystemSettingFormMap;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;

public interface ApiSystemSettingMapper  {

    public List<ApiSystemSettingFormMap> findSettingByPage(ApiSystemSettingFormMap apiSystemSettingFormMap);

	public void addEntity(ApiSystemSettingFormMap apiSystemSettingFormMap);

	public List<ApiSystemSettingFormMap> findByAttribute(String string, String sysKey,
			Class<ApiSystemSettingFormMap> class1);

	public void editEntity(ApiSystemSettingFormMap apiSystemSettingFormMap);

	public Object findbyFrist(String string, String id, Class<ApiSystemSettingFormMap> class1);
}
