package com.ysttench.application.auth.core.rdto;

import java.io.Serializable;


public class KcInfo implements Serializable{
	private static final long serialVersionUID = -4743180570777129639L;
	//物料编码
	String FMaterialnumber;
	//物料名称
	String FMaterialNAME;
	//仓库名称
	String FStockIdname;
	//批号
	String FLotname;
	//库存主单位
	String FStockUnitname;
	//库存量（主单位）
	String FBaseQty;
	//库存组织
	String FStockOrgname;
	//
	String FMaterialSpecification;
	
	public String getFMaterialSpecification() {
		return FMaterialSpecification;
	}
	public void setFMaterialSpecification(String fMaterialSpecification) {
		FMaterialSpecification = fMaterialSpecification;
	}
	public String getFMaterialnumber() {
		return FMaterialnumber;
	}
	public void setFMaterialnumber(String fMaterialnumber) {
		FMaterialnumber = fMaterialnumber;
	}
	public String getFMaterialNAME() {
		return FMaterialNAME;
	}
	public void setFMaterialNAME(String fMaterialNAME) {
		FMaterialNAME = fMaterialNAME;
	}
	public String getFStockIdname() {
		return FStockIdname;
	}
	public void setFStockIdname(String fStockIdname) {
		FStockIdname = fStockIdname;
	}
	public String getFLotname() {
		return FLotname;
	}
	public void setFLotname(String fLotname) {
		FLotname = fLotname;
	}
	public String getFStockUnitname() {
		return FStockUnitname;
	}
	public void setFStockUnitname(String fStockUnitname) {
		FStockUnitname = fStockUnitname;
	}
	public String getFBaseQty() {
		return FBaseQty;
	}
	public void setFBaseQty(String fBaseQty) {
		FBaseQty = fBaseQty;
	}
	public String getFStockOrgname() {
		return FStockOrgname;
	}
	public void setFStockOrgname(String fStockOrgname) {
		FStockOrgname = fStockOrgname;
	}
	
}
