package com.ysttench.application.htdytasties.core.rdto;

import java.io.Serializable;

/**
 * 接口返回类
 * @author howard
 *
 */
public class ResponseDto implements Serializable {

    private static final long serialVersionUID = 6945445819091582390L;

    private String errcode;
    private String errmsg;
    private Object responseObject;
    
    public String getErrcode() {
        return errcode;
    }
    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }
    public String getErrmsg() {
        return errmsg;
    }
    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
    public Object getResponseObject() {
        return responseObject;
    }
    public void setResponseObject(Object responseObject) {
        this.responseObject = responseObject;
    }
}
