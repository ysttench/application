package com.ysttench.application.common.mail.dto;

import java.math.BigDecimal;
import java.util.Date;

public class MailAttachBean {
    private BigDecimal mailId;
    private String attachType;
    private BigDecimal attachSize;
    private String attachFileName;
    private String attachDescription;
    private String attachPath;
    private Date createTime;
    private byte[] attachContent;
    
    public BigDecimal getMailId() {
        return mailId;
    }
    public void setMailId(BigDecimal mailId) {
        this.mailId = mailId;
    }
    public String getAttachType() {
        return attachType;
    }
    public void setAttachType(String attachType) {
        this.attachType = attachType;
    }
    public BigDecimal getAttachSize() {
        return attachSize;
    }
    public void setAttachSize(BigDecimal attachSize) {
        this.attachSize = attachSize;
    }
    public String getAttachFileName() {
        return attachFileName;
    }
    public void setAttachFileName(String attachFileName) {
        this.attachFileName = attachFileName;
    }
    public String getAttachDescription() {
        return attachDescription;
    }
    public void setAttachDescription(String attachDescription) {
        this.attachDescription = attachDescription;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public byte[] getAttachContent() {
        return attachContent;
    }
    public void setAttachContent(byte[] attachContent) {
        this.attachContent = attachContent;
    }
    public String getAttachPath() {
        return attachPath;
    }
    public void setAttachPath(String attachPath) {
        this.attachPath = attachPath;
    }

}
