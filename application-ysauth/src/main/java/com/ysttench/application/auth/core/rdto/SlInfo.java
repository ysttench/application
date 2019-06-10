package com.ysttench.application.auth.core.rdto;

import java.io.Serializable;


public class SlInfo implements Serializable{
	private static final long serialVersionUID = -4743180570777129639L;
	String FID;
	String FENTRYID;
	//单据编号
	String FBILLNO;
	//日期
	String FDATE;
	//单据状态
	String FDOCUMENTSTATUS;
	//发料组织
	String FSTOCKORGIDname;
	//生产组织
	String FPRDORGIDname;
	//物料编码
	String FMATERIALIDNumber;
	//物料名称
	String FMATERIALIDname;
	//车间
	//规格型号
	//单位
	String FSTOCKUNITname;
	//申请数量
	String FSTOCKAPPQTY;
	//实发数量
	String FSTOCKACTUALQTY;
	//仓库
	String FSTOCKname;
	//仓库id
	String STOCKID;
	//申请人
	String FCREATORname;
	//序列号
	
	String Xlh;
	
	public String getSTOCKID() {
		return STOCKID;
	}
	public void setSTOCKID(String sTOCKID) {
		STOCKID = sTOCKID;
	}
	public String getFID() {
		return FID;
	}
	public void setFID(String fID) {
		FID = fID;
	}
	public String getXlh() {
		return Xlh;
	}
	public void setXlh(String xlh) {
		Xlh = xlh;
	}
	public String getFCREATORname() {
		return FCREATORname;
	}
	public void setFCREATORname(String fCREATORname) {
		FCREATORname = fCREATORname;
	}
	public String getFBILLNO() {
		return FBILLNO;
	}
	public void setFBILLNO(String fBILLNO) {
		FBILLNO = fBILLNO;
	}
	public String getFDATE() {
		return FDATE;
	}
	public void setFDATE(String fDATE) {
		FDATE = fDATE;
	}
	public String getFENTRYID() {
		return FENTRYID;
	}
	public void setFENTRYID(String fENTRYID) {
		FENTRYID = fENTRYID;
	}
	public String getFDOCUMENTSTATUS() {
		return FDOCUMENTSTATUS;
	}
	public void setFDOCUMENTSTATUS(String fDOCUMENTSTATUS) {
		FDOCUMENTSTATUS = fDOCUMENTSTATUS;
	}
	public String getFSTOCKORGIDname() {
		return FSTOCKORGIDname;
	}
	public void setFSTOCKORGIDname(String fSTOCKORGIDname) {
		FSTOCKORGIDname = fSTOCKORGIDname;
	}
	public String getFPRDORGIDname() {
		return FPRDORGIDname;
	}
	public void setFPRDORGIDname(String fPRDORGIDname) {
		FPRDORGIDname = fPRDORGIDname;
	}
	public String getFMATERIALIDNumber() {
		return FMATERIALIDNumber;
	}
	public void setFMATERIALIDNumber(String fMATERIALIDNumber) {
		FMATERIALIDNumber = fMATERIALIDNumber;
	}
	public String getFMATERIALIDname() {
		return FMATERIALIDname;
	}
	public void setFMATERIALIDname(String fMATERIALIDname) {
		FMATERIALIDname = fMATERIALIDname;
	}
	public String getFSTOCKUNITname() {
		return FSTOCKUNITname;
	}
	public void setFSTOCKUNITname(String fSTOCKUNITname) {
		FSTOCKUNITname = fSTOCKUNITname;
	}
	public String getFSTOCKAPPQTY() {
		return FSTOCKAPPQTY;
	}
	public void setFSTOCKAPPQTY(String fSTOCKAPPQTY) {
		FSTOCKAPPQTY = fSTOCKAPPQTY;
	}
	public String getFSTOCKACTUALQTY() {
		return FSTOCKACTUALQTY;
	}
	public void setFSTOCKACTUALQTY(String fSTOCKACTUALQTY) {
		FSTOCKACTUALQTY = fSTOCKACTUALQTY;
	}
	public String getFSTOCKname() {
		return FSTOCKname;
	}
	public void setFSTOCKname(String fSTOCKname) {
		FSTOCKname = fSTOCKname;
	}
}
