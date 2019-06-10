package com.ysttench.application.auth.core.rdto;

import java.io.Serializable;


public class GdPdInfo implements Serializable{
	private static final long serialVersionUID = -4743180570777129639L;
	String FID;
	String FBILLNO;
	String FDATE;
	String FDOCUMENTSTATUS;
	String FENTRYID;
	String FNUMBER;
	String FASSETSNUMBER;
	String FNAME;
	String FSERIALNO;
	String FBaseAcctQty;
	String FBaseCountQty;
    String  PDTYPE;
	
	public String getPDTYPE() {
		return PDTYPE;
	}
	public void setPDTYPE(String pDTYPE) {
		PDTYPE = pDTYPE;
	}
	public String getFASSETSNUMBER() {
		return FASSETSNUMBER;
	}
	public void setFASSETSNUMBER(String fASSETSNUMBER) {
		FASSETSNUMBER = fASSETSNUMBER;
	}
	public String getFENTRYID() {
		return FENTRYID;
	}
	public void setFENTRYID(String fENTRYID) {
		FENTRYID = fENTRYID;
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
	public String getFSERIALNO() {
		return FSERIALNO;
	}
	public void setFSERIALNO(String fSERIALNO) {
		FSERIALNO = fSERIALNO;
	}
	public String getFBaseAcctQty() {
		return FBaseAcctQty;
	}
	public void setFBaseAcctQty(String fBaseAcctQty) {
		FBaseAcctQty = fBaseAcctQty;
	}
	public String getFBaseCountQty() {
		return FBaseCountQty;
	}
	public void setFBaseCountQty(String fBaseCountQty) {
		FBaseCountQty = fBaseCountQty;
	}
	public String getFID() {
		return FID;
	}
	public void setFID(String fID) {
		FID = fID;
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
	public String getFDOCUMENTSTATUS() {
		return FDOCUMENTSTATUS;
	}
	public void setFDOCUMENTSTATUS(String fDOCUMENTSTATUS) {
		FDOCUMENTSTATUS = fDOCUMENTSTATUS;
	}
	
}
