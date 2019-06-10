package com.ysttench.application.auth.settings.kernel.mapper;

import java.util.List;

import com.ysttench.application.auth.settings.kernel.entity.SysDepartmentFormMap;
import com.ysttench.application.database.ibatis.mapper.BaseMapper;

public interface SysDepartmentMapper  {

    public List<SysDepartmentFormMap> findParent(SysDepartmentFormMap sysDepartmentFormMap);
    
    public List<SysDepartmentFormMap> findAllData(SysDepartmentFormMap sysDepartmentFormMap);
    
    public SysDepartmentFormMap findDetailDepartment(SysDepartmentFormMap sysDepartmentFormMap);

    public List<SysDepartmentFormMap> findDepartmentByPage(SysDepartmentFormMap sysDepartmentFormMap);

    public String getCount(SysDepartmentFormMap sysDepartmentFormMap);

	public SysDepartmentFormMap findbyFrist(String string, String deptId, Class<SysDepartmentFormMap> class1);

	public List<SysDepartmentFormMap> findByWhere(SysDepartmentFormMap sysDepartmentFormMap);

	public void editEntity(SysDepartmentFormMap sysDepartmentFormMap);

	public void addEntity(SysDepartmentFormMap sysDepartmentFormMap);

	public List<SysDepartmentFormMap> findByAttribute(String string, String deptCode,
			Class<SysDepartmentFormMap> class1);

	public List<SysDepartmentFormMap> findByNames(SysDepartmentFormMap sysDepartmentFormMap);

}
