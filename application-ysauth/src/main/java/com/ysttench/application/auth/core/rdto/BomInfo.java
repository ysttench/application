package com.ysttench.application.auth.core.rdto;

import java.io.Serializable;


public class BomInfo implements Serializable{
	private static final long serialVersionUID = -4743180570777129639L;
	//生产用料清单编号
	String FBILLNO;
	//生产订单编号
	String FMOBILLNO;
	//物料编号
	String FNUMBER;
	//物料名称
	String FNAME;
	//需求数量
	String FNEEDQTY;
	//库存数量
	String STOCKQTY;
	String FENTRYID;
	//序列号
	String FSENO;
	//仓库id
	String STOCKID;
	
	public String getSTOCKID() {
		return STOCKID;
	}
	public void setSTOCKID(String sTOCKID) {
		STOCKID = sTOCKID;
	}
	public String getFSENO() {
		return FSENO;
	}
	public String getSTOCKQTY() {
		return STOCKQTY;
	}
	public void setSTOCKQTY(String sTOCKQTY) {
		STOCKQTY = sTOCKQTY;
	}
	public void setFSENO(String fSENO) {
		FSENO = fSENO;
	}
	public String getFENTRYID() {
		return FENTRYID;
	}
	public void setFENTRYID(String fENTRYID) {
		FENTRYID = fENTRYID;
	}
	public String getFBILLNO() {
		return FBILLNO;
	}
	public void setFBILLNO(String fBILLNO) {
		FBILLNO = fBILLNO;
	}
	public String getFMOBILLNO() {
		return FMOBILLNO;
	}
	public void setFMOBILLNO(String fMOBILLNO) {
		FMOBILLNO = fMOBILLNO;
	}
	public String getFNUMBER() {
		return FNUMBER;
	}
	public void setFNUMBER(String fNUMBER) {
		FNUMBER = fNUMBER;
	}
	public String getFNAME() {
		return FNAME;
	}
	public void setFNAME(String fNAME) {
		FNAME = fNAME;
	}
	public String getFNEEDQTY() {
		return FNEEDQTY;
	}
	public void setFNEEDQTY(String fNEEDQTY) {
		FNEEDQTY = fNEEDQTY;
	}
	
}
