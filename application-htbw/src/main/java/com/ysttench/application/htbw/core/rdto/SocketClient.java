package com.ysttench.application.htbw.core.rdto;

import java.net.Socket;

public class SocketClient {
    String equipmentId;
    String equipmentName;
    String equipmentNum;
    String status;
    String alarmMaxTemperature;
    String alarmMinTemperature;
    String alarmMinHumidity;
    String alarmMaxHumidity;
    Socket socket;
    String domainName;
    String temLimt;
    String humLimit;
    String volLimit;
    String equipmentIp;
    String equipmentPort;
    String equiptypeName;
    
    
    public String getEquiptypeName() {
        return equiptypeName;
    }

    public void setEquiptypeName(String equiptypeName) {
        this.equiptypeName = equiptypeName;
    }

    public String getEquipmentIp() {
        return equipmentIp;
    }

    public void setEquipmentIp(String equipmentIp) {
        this.equipmentIp = equipmentIp;
    }

    public String getEquipmentPort() {
        return equipmentPort;
    }

    public void setEquipmentPort(String equipmentPort) {
        this.equipmentPort = equipmentPort;
    }

    public String getTemLimt() {
        return temLimt;
    }

    public void setTemLimt(String temLimt) {
        this.temLimt = temLimt;
    }

    public String getHumLimit() {
        return humLimit;
    }

    public void setHumLimit(String humLimit) {
        this.humLimit = humLimit;
    }

    public String getVolLimit() {
        return volLimit;
    }

    public void setVolLimit(String volLimit) {
        this.volLimit = volLimit;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getAlarmMaxTemperature() {
        return alarmMaxTemperature;
    }

    public void setAlarmMaxTemperature(String alarmMaxTemperature) {
        this.alarmMaxTemperature = alarmMaxTemperature;
    }

    public String getAlarmMinTemperature() {
        return alarmMinTemperature;
    }

    public void setAlarmMinTemperature(String alarmMinTemperature) {
        this.alarmMinTemperature = alarmMinTemperature;
    }

    public String getAlarmMinHumidity() {
        return alarmMinHumidity;
    }

    public void setAlarmMinHumidity(String alarmMinHumidity) {
        this.alarmMinHumidity = alarmMinHumidity;
    }

    public String getAlarmMaxHumidity() {
        return alarmMaxHumidity;
    }

    public void setAlarmMaxHumidity(String alarmMaxHumidity) {
        this.alarmMaxHumidity = alarmMaxHumidity;
    }

    public String getEquipmentNum() {
	return equipmentNum;
    }

    public void setEquipmentNum(String equipmentNum) {
	this.equipmentNum = equipmentNum;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public Socket getSocket() {
	return socket;
    }

    public void setSocket(Socket socket) {
	this.socket = socket;
    }

    public String getEquipmentId() {
	return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
	this.equipmentId = equipmentId;
    }

    public String getEquipmentName() {
	return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
	this.equipmentName = equipmentName;
    }

}
