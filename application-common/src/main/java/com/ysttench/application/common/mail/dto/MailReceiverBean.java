package com.ysttench.application.common.mail.dto;

public class MailReceiverBean {
    private String receiverType;
    private String receiverName;
    private String receiverMail;
    
    public String getReceiverType() {
        return receiverType;
    }
    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }
    public String getReceiverName() {
        return receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public String getReceiverMail() {
        return receiverMail;
    }
    public void setReceiverMail(String receiverMail) {
        this.receiverMail = receiverMail;
    }
}
