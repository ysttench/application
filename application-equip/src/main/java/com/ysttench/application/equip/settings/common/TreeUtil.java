package com.ysttench.application.equip.settings.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ysttench.application.equip.settings.web.rdto.util.MenuTreeObject;



/**
 * ��һ��list����,�����bean���� parentId תΪ����ʽ
 * 
 */
public class TreeUtil {

    /**
     * ���ݸ��ڵ��ID��ȡ�����ӽڵ�
     * 
     * @param list
     *            �����
     * @param typeId
     *            ����ĸ��ڵ�ID
     * @return String
     */
    public List<MenuTreeObject> getChildTreeObjects(List<MenuTreeObject> list, int praentId) {
        List<MenuTreeObject> returnList = new ArrayList<MenuTreeObject>();
        for (Iterator<MenuTreeObject> iterator = list.iterator(); iterator.hasNext();) {
            MenuTreeObject t = (MenuTreeObject) iterator.next();
            // һ�����ݴ����ĳ�����ڵ�ID,�����ø��ڵ�������ӽڵ�
            if (t.getParentId() == praentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * �ݹ��б�
     * 
     * @param list
     * @param MenuTreeObject
     */
    private void recursionFn(List<MenuTreeObject> list, MenuTreeObject t) {
        List<MenuTreeObject> childList = getChildList(list, t);// �õ��ӽڵ��б�
        t.setChildren(childList);
        for (MenuTreeObject tChild : childList) {
            if (hasChild(list, tChild)) {// �ж��Ƿ����ӽڵ�
                // returnList.add(TreeObject);
                Iterator<MenuTreeObject> it = childList.iterator();
                while (it.hasNext()) {
                    MenuTreeObject n = (MenuTreeObject) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }

    // �õ��ӽڵ��б�
    private List<MenuTreeObject> getChildList(List<MenuTreeObject> list, MenuTreeObject t) {

        List<MenuTreeObject> tlist = new ArrayList<MenuTreeObject>();
        Iterator<MenuTreeObject> it = list.iterator();
        while (it.hasNext()) {
            MenuTreeObject n = (MenuTreeObject) it.next();
            if (n.getParentId() == t.getId()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    List<MenuTreeObject> returnList = new ArrayList<MenuTreeObject>();

    /**
     * ���ݸ��ڵ��ID��ȡ�����ӽڵ�
     * 
     * @param list
     *            �����
     * @param typeId
     *            ����ĸ��ڵ�ID
     * @param prefix
     *            �ӽڵ�ǰ׺
     */
    public List<MenuTreeObject> getChildTreeObjects(List<MenuTreeObject> list, int typeId, String prefix) {
        if (list == null)
            return null;
        for (Iterator<MenuTreeObject> iterator = list.iterator(); iterator.hasNext();) {
            MenuTreeObject node = (MenuTreeObject) iterator.next();
            // һ�����ݴ����ĳ�����ڵ�ID,�����ø��ڵ�������ӽڵ�
            if (node.getParentId() == typeId) {
                recursionFn(list, node, prefix);
            }
            // �����������еĸ��ڵ��µ������ӽڵ�
            /*
             * if (node.getParentId()==0) { recursionFn(list, node); }
             */
        }
        return returnList;
    }

    private void recursionFn(List<MenuTreeObject> list, MenuTreeObject node, String p) {
        List<MenuTreeObject> childList = getChildList(list, node);// �õ��ӽڵ��б�
        if (hasChild(list, node)) {// �ж��Ƿ����ӽڵ�
            returnList.add(node);
            Iterator<MenuTreeObject> it = childList.iterator();
            while (it.hasNext()) {
                MenuTreeObject n = (MenuTreeObject) it.next();
                n.setName(p + n.getName());
                recursionFn(list, n, p + p);
            }
        } else {
            returnList.add(node);
        }
    }

    // �ж��Ƿ����ӽڵ�
    private boolean hasChild(List<MenuTreeObject> list, MenuTreeObject t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }

    // ����ģ�����ݲ���
    public void main(String[] args) {
        /*
         * long start = System.currentTimeMillis(); List<TreeObject>
         * TreeObjectList = new ArrayList<TreeObject>();
         * 
         * TreeObjectUtil mt = new TreeObjectUtil(); List<TreeObject>
         * ns=mt.getChildTreeObjects(TreeObjectList,0); for (TreeObject m : ns)
         * { System.out.println(m.getName());
         * System.out.println(m.getChildren()); } long end =
         * System.currentTimeMillis(); System.out.println("��ʱ:" + (end - start)
         * + "ms");
         */
    }

}
