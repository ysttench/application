package com.ysttench.application.center.settings.web.rdto.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个是列表树形式显示的实体, 这里的字段是在前台显示所有的,可修改
 */
public class MenuTreeObject {
    private Integer id;
    private Integer menuId;
    private Integer parentId;
    private String name;
    private String parentName;
    private String menuKey;
    private String menuUrl;
    private String type;
    private String description;
    private Integer isHide;
    private List<MenuTreeObject> children = new ArrayList<MenuTreeObject>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<MenuTreeObject> getChildren() {
        return children;
    }

    public void setChildren(List<MenuTreeObject> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public Integer getIsHide() {
        return isHide;
    }

    public void setIsHide(Integer isHide) {
        this.isHide = isHide;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

}
