package com.ysttench.application.auth.core.rdto;

import java.io.Serializable;


/**
 * 用户信息
 * @author 潘益孟
 *
 */
public class UserInfo implements Serializable {
    
    private static final long serialVersionUID = -4743180570777129639L;
    private String userId;
    private String userName;
    private String password;
    private String name;
    private String sfzh;
    private String sex;
    private String phone;
    private String email;
    private byte[] avatar;
    private String comment;
    private String enabledFlag;
   // private Date enabledTime;
    private String deleteFlag;
    //private Date deleteTime;
   // private Date createTime;
    //private Date updateTime;
    private String policeId;
    private String deptName;
    private String deptId;
    
    public String getPoliceId() {
        return policeId;
    }
    public void setPoliceId(String policeId) {
        this.policeId = policeId;
    }
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    public String getDeptId() {
        return deptId;
    }
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSfzh() {
        return sfzh;
    }
    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
    public byte[] getAvatar() {
        return avatar;
    }
    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
    public String getEnabledFlag() {
        return enabledFlag;
    }
    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    /*public Date getEnabledTime() {
        return enabledTime;
    }
    public void setEnabledTime(Date enabledTime) {
        this.enabledTime = enabledTime;
    }*/
    public String getDeleteFlag() {
        return deleteFlag;
    }
    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
/*    public Date getDeleteTime() {
        return deleteTime;
    }
    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }*/

}
