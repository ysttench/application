package com.ysttench.application.auth.settings.web.controller.common;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.ysttench.application.auth.settings.kernel.entity.SysCodeConfigFormMap;
import com.ysttench.application.auth.settings.kernel.mapper.SysCodeConfigMapper;
import com.ysttench.application.auth.settings.web.rdto.system.SysCodeConfig;
import com.ysttench.application.database.ibatis.util.FormMapUtil;


/**
 * @author Howard 类说明：参数配置控制类
 */
@Component
public class CodeConfigComponent {
    @Inject
    private SysCodeConfigMapper sysCodeConfigMapper;

    /**
     * 获取父类Form值
     * 
     * @param configCode
     * @return
     */
    public SysCodeConfigFormMap getParentFormMap(String parentConfigCode) {
        SysCodeConfigFormMap sysCodeConfigFormMap = new SysCodeConfigFormMap();
        sysCodeConfigFormMap.put("configCode", parentConfigCode);
        sysCodeConfigFormMap.put("parentId", 0);
        List<SysCodeConfigFormMap> listscc = sysCodeConfigMapper.findByNames(sysCodeConfigFormMap);
        return listscc.get(0);
    }

    /**
     * 获取父类Form值
     * 
     * @param parentId
     * @return
     */
    public SysCodeConfigFormMap getParentFormMap(Integer parentId) {
        SysCodeConfigFormMap sysCodeConfigFormMap = new SysCodeConfigFormMap();
        sysCodeConfigFormMap.put("id", parentId);
        sysCodeConfigFormMap.put("parentId", 0);
        List<SysCodeConfigFormMap> listscc = sysCodeConfigMapper.findByNames(sysCodeConfigFormMap);
        return listscc.get(0);
    }

    /**
     * 指定父类编码获取子类数据列表
     * 
     * @param configCode
     * @return
     */
    public List<SysCodeConfig> getCodeListByConfigCode(String configCode) {
        SysCodeConfigFormMap paramFormMap = this.getParentFormMap(configCode);
        SysCodeConfigFormMap sysCodeConfigFormMap = new SysCodeConfigFormMap();
        sysCodeConfigFormMap.put("parentId", paramFormMap.getInt("id"));
        List<SysCodeConfigFormMap> mps = sysCodeConfigMapper.findByNames(sysCodeConfigFormMap);
        List<SysCodeConfig> list = new ArrayList<SysCodeConfig>();
        for (SysCodeConfigFormMap map : mps) {
            SysCodeConfig sysCodeConfig = new SysCodeConfig();
            FormMapUtil.flushObject(sysCodeConfig, map);
            list.add(sysCodeConfig);
        }
        return list;
    }

    /**
     * 根据父类编码，获取注解信息
     * 
     * @param configCode
     * @return
     */
    public String getDiscriptionByConfigCode(String configCode) {
        SysCodeConfigFormMap scc = sysCodeConfigMapper.findbyFrist("configCode", configCode,
                SysCodeConfigFormMap.class);
        String responseString = "";
        if (!scc.isEmpty()) {
            responseString = scc.getStr("description");
        }
        return responseString;
    }

    /**
     * 根据父类编码，获取数值信息
     * 
     * @param configCode
     * @return
     */
    public String getConfigValueByConfigCode(String configCode) {
        SysCodeConfigFormMap scc = sysCodeConfigMapper.findbyFrist("configCode", configCode,
                SysCodeConfigFormMap.class);
        String responseString = "";
        if (!scc.isEmpty()) {
            responseString = scc.getStr("configValue");
        }
        return responseString;
    }

    /**
     * 根据父类编码及数值获取参数配置名称
     * 
     * @param configCode
     * @return
     */
    public String getConfigName(String configCode, String configValue) {
        SysCodeConfigFormMap paramMap = new SysCodeConfigFormMap();
        paramMap.put("configCode", configCode);
        paramMap.put("configValue", configValue);
        List<SysCodeConfigFormMap> listScc = sysCodeConfigMapper.findConfigName(paramMap);
        if (!listScc.isEmpty() && listScc.size() > 0) {
            return listScc.get(0).getStr("configName");
        }
        return AttrConstants.ERROR;
    }

    /**
     * 根据父类编码及数值获取参数配置中的主键ID
     * 
     * @param configCode
     * @param configValue
     * @return
     */
    public int getConfigId(String configCode, String configValue) {
        SysCodeConfigFormMap paramMap = new SysCodeConfigFormMap();
        paramMap.put("configCode", configCode);
        paramMap.put("configValue", configValue);
        List<SysCodeConfigFormMap> listScc = sysCodeConfigMapper.findConfigName(paramMap);
        if (!listScc.isEmpty() && listScc.size() > 0) {
            return listScc.get(0).getInt("id");
        }
        return 0;
    }
}