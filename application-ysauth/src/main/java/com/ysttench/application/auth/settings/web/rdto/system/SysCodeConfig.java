package com.ysttench.application.auth.settings.web.rdto.system;

import java.io.Serializable;
import java.util.Date;

public class SysCodeConfig implements Serializable {

    private static final long serialVersionUID = 6945445819091582390L;

    private Integer id;
    private String configValue;
    private String configName;
    private String configCode;
    private Integer parentId;
    private String detailFlag;
    private String description;
    private Integer displayOrder;
    private String deletedFlag;
    private Date createTime;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getConfigValue() {
        return configValue;
    }
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
    public String getConfigName() {
        return configName;
    }
    public void setConfigName(String configName) {
        this.configName = configName;
    }
    public String getConfigCode() {
        return configCode;
    }
    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }
    public Integer getParentId() {
        return parentId;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public String getDetailFlag() {
        return detailFlag;
    }
    public void setDetailFlag(String detailFlag) {
        this.detailFlag = detailFlag;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    public String getDeletedFlag() {
		return deletedFlag;
	}
	public void setDeletedFlag(String deletedFlag) {
		this.deletedFlag = deletedFlag;
	}
	public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
	@Override
	public String toString() {
		return "SysCodeConfig [id=" + id + ", configValue=" + configValue
				+ ", configName=" + configName + ", configCode=" + configCode
				+ ", parentId=" + parentId + ", detailFlag=" + detailFlag
				+ ", description=" + description + ", displayOrder="
				+ displayOrder + ", deletedFlag=" + deletedFlag
				+ ", createTime=" + createTime + "]";
	}

    
}
