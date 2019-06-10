package com.ysttench.application.auth.core.rdto;

import java.io.Serializable;

public class StkInfo implements Serializable{
	private static final long serialVersionUID = -4743180570777129639L;
	String paging;
	//id
	String FID;
	//单据编号
	String FBILLNO;
	//入库日期
	String FDATE;
	//供应商名称
	String FGYName;
	//单据状态
	String STATUS;
	//物料编码
	String fMateriaNumber;
	//物料名称
	String FMaterialName;
	//库存单位
	String fUnitName;
	//实收数量
	String FNUMBER;
	//批号
	String FLOT_TEXT;
	//仓库
	String FName;
	
	String FNumber;
	String FREALQTY;
	String FSTOCKID;
	String FLOT;
	String FPOORDERNO;
	
	
	public String getPaging() {
		return paging;
	}
	public void setPaging(String paging) {
		this.paging = paging;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getFNUMBER() {
		return FNUMBER;
	}
	public void setFNUMBER(String fNUMBER) {
		FNUMBER = fNUMBER;
	}
	public String getFID() {
		return FID;
	}
	public void setFID(String fID) {
		FID = fID;
	}
	public String getFDATE() {
		return FDATE;
	}
	public void setFDATE(String fDATE) {
		FDATE = fDATE;
	}
	public String getFBILLNO() {
		return FBILLNO;
	}
	public void setFBILLNO(String fBILLNO) {
		FBILLNO = fBILLNO;
	}
	public String getFMaterialName() {
		return FMaterialName;
	}
	public void setFMaterialName(String fMaterialName) {
		FMaterialName = fMaterialName;
	}
	public String getFNumber() {
		return FNumber;
	}
	public void setFNumber(String fNumber) {
		FNumber = fNumber;
	}
	public String getFREALQTY() {
		return FREALQTY;
	}
	public void setFREALQTY(String fREALQTY) {
		FREALQTY = fREALQTY;
	}
	public String getFSTOCKID() {
		return FSTOCKID;
	}
	public void setFSTOCKID(String fSTOCKID) {
		FSTOCKID = fSTOCKID;
	}
	public String getFLOT() {
		return FLOT;
	}
	public void setFLOT(String fLOT) {
		FLOT = fLOT;
	}
	public String getFLOT_TEXT() {
		return FLOT_TEXT;
	}
	public void setFLOT_TEXT(String fLOT_TEXT) {
		FLOT_TEXT = fLOT_TEXT;
	}
	public String getFPOORDERNO() {
		return FPOORDERNO;
	}
	public void setFPOORDERNO(String fPOORDERNO) {
		FPOORDERNO = fPOORDERNO;
	}
	public String getFName() {
		return FName;
	}
	public void setFName(String fName) {
		FName = fName;
	}
	public String getFGYName() {
		return FGYName;
	}
	public void setFGYName(String fGYName) {
		FGYName = fGYName;
	}
	public String getfMateriaNumber() {
		return fMateriaNumber;
	}
	public void setfMateriaNumber(String fMateriaNumber) {
		this.fMateriaNumber = fMateriaNumber;
	}
	public String getfUnitName() {
		return fUnitName;
	}
	public void setfUnitName(String fUnitName) {
		this.fUnitName = fUnitName;
	}
	
}
