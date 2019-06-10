package com.ysttench.application.auth.core.rdto;

import java.io.Serializable;


public class CkInfo implements Serializable{
	private static final long serialVersionUID = -4743180570777129639L;
	String FID;
	String FNAME;
	public String getFID() {
		return FID;
	}
	public void setFID(String fID) {
		FID = fID;
	}
	public String getFNAME() {
		return FNAME;
	}
	public void setFNAME(String fNAME) {
		FNAME = fNAME;
	}
	
	
}
