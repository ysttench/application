package com.ysttench.application.htdytasties.settings.web.rdto.util;

import java.util.ArrayList;
import java.util.List;

/**
 * ǰ̨���͵���һ���������� ��̨��Ҫ���¶������ ����: menus[0].id 1 menus[0].level 1 menus[1].id 3
 * menus[1].level 2 menus[2].id 2 menus[2].level 3
 * ����˵��������Ҫ�����������,�����������һ��list<Resourcess>
 * 
 * @author Howard
 */
public class Params {

    private List<String> resId = new ArrayList<String>();
    private List<String> ids = new ArrayList<String>();
    private List<String> id = new ArrayList<String>();
    private List<String> rowId = new ArrayList<String>();

    public List<String> getResId() {
        return resId;
    }

    public void setResId(List<String> resId) {
        this.resId = resId;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public List<String> getRowId() {
        return rowId;
    }

    public void setRowId(List<String> rowId) {
        this.rowId = rowId;
    }

}
