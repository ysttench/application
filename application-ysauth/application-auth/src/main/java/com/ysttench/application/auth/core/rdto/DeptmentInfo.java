package com.ysttench.application.auth.core.rdto;

public class DeptmentInfo {
    private String id;
    private String parentId;
    private String deptCode;
    private String deptName;
    private String xzqh;
    private String status;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getDeptCode() {
        return deptCode;
    }
    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getXzqh() {
        return xzqh;
    }
    public void setXzqh(String xzqh) {
        this.xzqh = xzqh;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    

}
