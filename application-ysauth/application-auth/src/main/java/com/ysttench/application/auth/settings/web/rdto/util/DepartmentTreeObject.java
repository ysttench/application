package com.ysttench.application.auth.settings.web.rdto.util;

import java.util.ArrayList;
import java.util.List;

public class DepartmentTreeObject {
    private String id;
    private String parentId;
    private String label;
    private String value;
    private List<DepartmentTreeObject> children =new ArrayList<DepartmentTreeObject>();
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public List<DepartmentTreeObject> getChildren() {
        return children;
    }
    public void setChildren(List<DepartmentTreeObject> children) {
        this.children = children;
    }
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
    

}
