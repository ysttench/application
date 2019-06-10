package com.ysttench.application.auth.core.rdto;

import java.io.Serializable;


public class CwInfo implements Serializable{
	private static final long serialVersionUID = -4743180570777129639L;
	String BINNO;
	String BINNAME;
	public String getBINNO() {
		return BINNO;
	}
	public void setBINNO(String bINNO) {
		BINNO = bINNO;
	}
	public String getBINNAME() {
		return BINNAME;
	}
	public void setBINNAME(String bINNAME) {
		BINNAME = bINNAME;
	}
	
}
