package com.ysttench.application.auth.settings.web.controller.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ysttench.application.auth.settings.web.rdto.util.DepartmentTreeObject;



/**
 * 把一个list集合,里面的bean含有 parentId 转为树形式
 * 
 */
public class TreeUtilOfDepartment {

    /**
     * 根据父节点的ID获取所有子节点
     * 
     * @param list
     *            分类表
     * @param typeId
     *            传入的父节点ID
     * @return String
     */
    public List<DepartmentTreeObject> getChildTreeObjects(List<DepartmentTreeObject> list, String parentId) {
        List<DepartmentTreeObject> returnList = new ArrayList<DepartmentTreeObject>();
        for (Iterator<DepartmentTreeObject> iterator = list.iterator(); iterator.hasNext();) {
            DepartmentTreeObject t = (DepartmentTreeObject) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     * 5
     * @param list
     * @param MessageTreeObject
     */
    private void recursionFn(List<DepartmentTreeObject> list, DepartmentTreeObject t) {
        List<DepartmentTreeObject> childList = getChildList(list, t);// 得到子节点列表
        t.setChildren(childList);
        for (DepartmentTreeObject tChild : childList) {
            if (hasChild(list, tChild)) {// 判断是否有子节点
                // returnList.add(TreeObject);
                Iterator<DepartmentTreeObject> it = childList.iterator();
                while (it.hasNext()) {
                    DepartmentTreeObject n = (DepartmentTreeObject) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    // 得到子节点列表
    private List<DepartmentTreeObject> getChildList(List<DepartmentTreeObject> list, DepartmentTreeObject t) {

        List<DepartmentTreeObject> tlist = new ArrayList<DepartmentTreeObject>();
        Iterator<DepartmentTreeObject> it = list.iterator();
        while (it.hasNext()) {
            DepartmentTreeObject n = (DepartmentTreeObject) it.next();
            if (n.getParentId().equals(t.getId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    List<DepartmentTreeObject> returnList = new ArrayList<DepartmentTreeObject>();


    // 判断是否有子节点
    private boolean hasChild(List<DepartmentTreeObject> list, DepartmentTreeObject t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }


}
