package com.ysttench.application.auth.core.rdto;

import java.io.Serializable;


public class PdCkInfo implements Serializable{
	private static final long serialVersionUID = -4743180570777129639L;
	String FNUMBER;
	String FNAME;
	
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
	
	
}
