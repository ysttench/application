package com.ysttench.application.common.mail.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MailClientBean {
    private BigDecimal mailId;
    // 发件人
    private String fromName;
    private String fromAddress;
    // 全体收件人
    private List<MailReceiverBean> listReceiveAddress;
    // 收件人
    private String receiveAddressTO;
    private List<MailReceiverBean> listReceiveAddressTO;
    // 抄送人
    private String receiveAddressCC;
    private List<MailReceiverBean> listReceiveAddressCC;
    // 暗送人  
    private String receiveAddressBCC;
    private List<MailReceiverBean> listReceiveAddressBCC;
    // 主题
    private String subject;
    // 纯文本邮件正文
    private String text;
    // 非纯文本邮件正文
    private String content;
    // 邮件正文编码格式
    private String contentType;
    // 发送时间
    private Date sentDate;
    private String strSentDate;
    // 接收时间
    private Date receivedDate;
    private String strReceivedDate;
    // 是否已读
    private boolean isSeen;
    // 邮件优先级
    private String priority;
    // 是否需要回执
    private boolean replySign;
    // 部分的字节大小
    private int mailSize;
    // 部分的行数
    private int lineCount;
    // 是否包含附件
    private boolean containAttachment;
    // 附件流
    private Map<String, String> mapAttachment;
    private List<MailAttachBean> listAttachment;
    // 发件人
    private String sender;
    // 回复地址
    private String replyTo;
    // 标示部分是否为附件或者是否应当在内部显示
    private String disposition;
    private String encoding;
    private String contentId;
    private String contentMd5;
    // 部分的简短文本概括说明
    private String description;
    private String contentLanguage;
    private String messageId;
    // 附件的文件名
    private String fileName;
    private String createTime;
    
    // 发送邮件用
    // 审核标志：0：草稿；1：发送
    private String sendFlag;
    // 处理标志：0：未发送；1：已发送；100：发送错误（send_flag=1）
    private String dealFlag;
    private String dealTime;
    // 删除标记：0：无删除标记；1：有删除标记
    private String deleteFlag;
    private String deleteTime;
    
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public boolean isSeen() {
        return isSeen;
    }
    public void setSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public String getReceiveAddressTO() {
        return receiveAddressTO;
    }
    public void setReceiveAddressTO(String receiveAddressTO) {
        this.receiveAddressTO = receiveAddressTO;
    }
    public String getReceiveAddressCC() {
        return receiveAddressCC;
    }
    public void setReceiveAddressCC(String receiveAddressCC) {
        this.receiveAddressCC = receiveAddressCC;
    }
    public String getReceiveAddressBCC() {
        return receiveAddressBCC;
    }
    public void setReceiveAddressBCC(String receiveAddressBCC) {
        this.receiveAddressBCC = receiveAddressBCC;
    }
    public Map<String, String> getMapAttachment() {
        return mapAttachment;
    }
    public void setMapAttachment(Map<String, String> mapAttachment) {
        this.mapAttachment = mapAttachment;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    public int getLineCount() {
        return lineCount;
    }
    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getReplyTo() {
        return replyTo;
    }
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
    public String getDisposition() {
        return disposition;
    }
    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }
    public String getEncoding() {
        return encoding;
    }
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getContentLanguage() {
        return contentLanguage;
    }
    public void setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFromName() {
        return fromName;
    }
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }
    public String getFromAddress() {
        return fromAddress;
    }
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
    public List<MailReceiverBean> getListReceiveAddressTO() {
        return listReceiveAddressTO;
    }
    public void setListReceiveAddressTO(List<MailReceiverBean> listReceiveAddressTO) {
        this.listReceiveAddressTO = listReceiveAddressTO;
    }
    public List<MailReceiverBean> getListReceiveAddressCC() {
        return listReceiveAddressCC;
    }
    public void setListReceiveAddressCC(List<MailReceiverBean> listReceiveAddressCC) {
        this.listReceiveAddressCC = listReceiveAddressCC;
    }
    public List<MailReceiverBean> getListReceiveAddressBCC() {
        return listReceiveAddressBCC;
    }
    public void setListReceiveAddressBCC(List<MailReceiverBean> listReceiveAddressBCC) {
        this.listReceiveAddressBCC = listReceiveAddressBCC;
    }
    public List<MailReceiverBean> getListReceiveAddress() {
        return listReceiveAddress;
    }
    public void setListReceiveAddress(List<MailReceiverBean> listReceiveAddress) {
        this.listReceiveAddress = listReceiveAddress;
    }
    public List<MailAttachBean> getListAttachment() {
        return listAttachment;
    }
    public void setListAttachment(List<MailAttachBean> listAttachment) {
        this.listAttachment = listAttachment;
    }
    public boolean isReplySign() {
        return replySign;
    }
    public void setReplySign(boolean replySign) {
        this.replySign = replySign;
    }
    public int getMailSize() {
        return mailSize;
    }
    public void setMailSize(int mailSize) {
        this.mailSize = mailSize;
    }
    public boolean isContainAttachment() {
        return containAttachment;
    }
    public void setContainAttachment(boolean containAttachment) {
        this.containAttachment = containAttachment;
    }
    public String getContentId() {
        return contentId;
    }
    public void setContentId(String contentId) {
        this.contentId = contentId;
    }
    public String getContentMd5() {
        return contentMd5;
    }
    public void setContentMd5(String contentMd5) {
        this.contentMd5 = contentMd5;
    }
    public String getMessageId() {
        return messageId;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public String getStrSentDate() {
        return strSentDate;
    }
    public void setStrSentDate(String strSentDate) {
        this.strSentDate = strSentDate;
    }
    public String getStrReceivedDate() {
        return strReceivedDate;
    }
    public void setStrReceivedDate(String strReceivedDate) {
        this.strReceivedDate = strReceivedDate;
    }
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }
    public Date getSentDate() {
        return sentDate;
    }
    public Date getReceivedDate() {
        return receivedDate;
    }
    public String getSendFlag() {
        return sendFlag;
    }
    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag;
    }
    public String getDealFlag() {
        return dealFlag;
    }
    public void setDealFlag(String dealFlag) {
        this.dealFlag = dealFlag;
    }
    public String getDeleteFlag() {
        return deleteFlag;
    }
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    public String getDealTime() {
        return dealTime;
    }
    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }
    public String getDeleteTime() {
        return deleteTime;
    }
    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public BigDecimal getMailId() {
        return mailId;
    }
    public void setMailId(BigDecimal mailId) {
        this.mailId = mailId;
    }

}
