package com.ysttench.application.equip.core.rdto;

public class EquipException {
String equipmentNum;
String exceptCode;
String startTime;
String endTime;
String description;
String dracution;
public String getEquipmentNum() {
	return equipmentNum;
}
public void setEquipmentNum(String equipmentNum) {
	this.equipmentNum = equipmentNum;
}
public String getExceptCode() {
	return exceptCode;
}
public void setExceptCode(String exceptCode) {
	this.exceptCode = exceptCode;
}
public String getStartTime() {
	return startTime;
}
public void setStartTime(String startTime) {
	this.startTime = startTime;
}
public String getEndTime() {
	return endTime;
}
public void setEndTime(String endTime) {
	this.endTime = endTime;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getDracution() {
	return dracution;
}
public void setDracution(String dracution) {
	this.dracution = dracution;
}

}
