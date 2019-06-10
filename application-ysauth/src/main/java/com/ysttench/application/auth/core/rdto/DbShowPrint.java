package com.ysttench.application.auth.core.rdto;

public class DbShowPrint {
	private String errcode;
	private String errmsg;
	private String fseq;
	private String flot;
	private String fmaterialid;
	private String fremark;

	public String getFseq() {
		return fseq;
	}

	public void setFseq(String fseq) {
		this.fseq = fseq;
	}

	public String getFlot() {
		return flot;
	}

	public void setFlot(String flot) {
		this.flot = flot;
	}

	public String getFmaterialid() {
		return fmaterialid;
	}

	public void setFmaterialid(String fmaterialid) {
		this.fmaterialid = fmaterialid;
	}

	public String getFremark() {
		return fremark;
	}

	public void setFremark(String fremark) {
		this.fremark = fremark;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

}
