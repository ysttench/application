package com.ysttench.application.common.util.excel;

import java.io.Serializable;

/**
 * 类说明：上传文件相应返回封装类
 * 
 * @author Howard
 * 
 */
public class ParseExcelResult implements Serializable {
    private static final long serialVersionUID = 8414911135893385776L;
    private int code;
    private String msg;
    private String fileId;// 文件路径
    private String[] fieldName;// 导入文件字段数据
    private String[][] valueArray;// 存放 字段 及字段数据
    private long rowNum;// 总行数
    private Boolean fitted = false;
    private int[] headorders;
    private Object dataCustomFormMaps;
    private Object dataCustomFieldFormMaps;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String[] getFieldName() {
        return fieldName;
    }

    public void setFieldName(String[] fieldName) {
        this.fieldName = fieldName;
    }

    public String[][] getValueArray() {
        return valueArray;
    }

    public void setValueArray(String[][] valueArray) {
        this.valueArray = valueArray;
    }

    public long getRowNum() {
        return rowNum;
    }

    public void setRowNum(long rowNum) {
        this.rowNum = rowNum;
    }

    public Boolean getFitted() {
        return fitted;
    }

    public void setFitted(Boolean fitted) {
        this.fitted = fitted;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int[] getHeadorders() {
        return headorders;
    }

    public void setHeadorders(int[] headorders) {
        this.headorders = headorders;
    }

    public Object getDataCustomFormMaps() {
        return dataCustomFormMaps;
    }

    public void setDataCustomFormMaps(Object dataCustomFormMaps) {
        this.dataCustomFormMaps = dataCustomFormMaps;
    }

    public Object getDataCustomFieldFormMaps() {
        return dataCustomFieldFormMaps;
    }

    public void setDataCustomFieldFormMaps(Object dataCustomFieldFormMaps) {
        this.dataCustomFieldFormMaps = dataCustomFieldFormMaps;
    }
    
}
