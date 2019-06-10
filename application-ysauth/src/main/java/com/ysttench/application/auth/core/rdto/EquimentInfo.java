package com.ysttench.application.auth.core.rdto;

public class EquimentInfo {
	private String id;
	private String equimentName;
	private String modubusAdress;
	private String deleteFlag;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEquimentName() {
		return equimentName;
	}

	public void setEquimentName(String equimentName) {
		this.equimentName = equimentName;
	}

	public String getModubusAdress() {
		return modubusAdress;
	}

	public void setModubusAdress(String modubusAdress) {
		this.modubusAdress = modubusAdress;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

}
