package com.ysttench.application.auth.core.rdto;

import java.io.Serializable;


public class GdInfo implements Serializable{
	private static final long serialVersionUID = -4743180570777129639L;
	//卡片编码
	String FNUMBER;
	//资产数量
	String FQUANTITY;
	//数量单位
	String UNITNAME;
	//时间
	String FCREATEDATE;
	//资产分类 
	String TYPENAME;
	//资产名称
	String FNAME;
	//id
	String FPKID;
	//资产编码
	String FASSETNO;
	//资产位置
	String  PLACENAME;
	//保管人
	String  USERNAME;
	//所属部门
	String  DEPARTNAME;
	
	public String getDEPARTNAME() {
		return DEPARTNAME;
	}
	public void setDEPARTNAME(String dEPARTNAME) {
		DEPARTNAME = dEPARTNAME;
	}
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	public String getFNUMBER() {
		return FNUMBER;
	}
	public void setFNUMBER(String fNUMBER) {
		FNUMBER = fNUMBER;
	}
	public String getFQUANTITY() {
		return FQUANTITY;
	}
	public void setFQUANTITY(String fQUANTITY) {
		FQUANTITY = fQUANTITY;
	}
	public String getUNITNAME() {
		return UNITNAME;
	}
	public void setUNITNAME(String uNITNAME) {
		UNITNAME = uNITNAME;
	}
	public String getFCREATEDATE() {
		return FCREATEDATE;
	}
	public void setFCREATEDATE(String fCREATEDATE) {
		FCREATEDATE = fCREATEDATE;
	}
	public String getTYPENAME() {
		return TYPENAME;
	}
	public void setTYPENAME(String tYPENAME) {
		TYPENAME = tYPENAME;
	}
	public String getFNAME() {
		return FNAME;
	}
	public void setFNAME(String fNAME) {
		FNAME = fNAME;
	}
	public String getFPKID() {
		return FPKID;
	}
	public void setFPKID(String fPKID) {
		FPKID = fPKID;
	}
	public String getFASSETNO() {
		return FASSETNO;
	}
	public void setFASSETNO(String fASSETNO) {
		FASSETNO = fASSETNO;
	}
	public String getPLACENAME() {
		return PLACENAME;
	}
	public void setPLACENAME(String pLACENAME) {
		PLACENAME = pLACENAME;
	}
	
}
