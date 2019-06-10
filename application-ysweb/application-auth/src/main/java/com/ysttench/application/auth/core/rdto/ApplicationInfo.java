package com.ysttench.application.auth.core.rdto;

import java.io.Serializable;

/**应用子系统信息
 * @author gengyong
 *
 */
public class ApplicationInfo implements Serializable {

    private static final long serialVersionUID = 8686846929970322858L;
    
    private String sysKey;
    
    private String apiName;
    
    private String apiUrl;
    
    private String apiDeveloper;
    
    private String departmentId;
    
    public String getSysKey() {
        return sysKey;
    }

    public void setSysKey(String sysKey) {
        this.sysKey = sysKey;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiDeveloper() {
        return apiDeveloper;
    }

    public void setApiDeveloper(String apiDeveloper) {
        this.apiDeveloper = apiDeveloper;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private String comment;
    

    
    

}
