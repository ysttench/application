package com.ysttench.application.database.jdbc.connect.base;

public class ConnectInfo {
    private String serviceName;
    private String connectStr;
    private int port;
    private String userName;
    private String password;
    private String hostName;
    private int dsType;
    private String dsParameter;
    private String dblinkName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getConnectStr() {
        return connectStr;
    }

    public void setConnectStr(String connectStr) {
        this.connectStr = connectStr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getDsType() {
        return dsType;
    }

    public void setDsType(int dsType) {
        this.dsType = dsType;
    }

    public String getDsParameter() {
        return dsParameter;
    }

    public void setDsParameter(String dsParameter) {
        this.dsParameter = dsParameter;
    }

    public String getDblinkName() {
        return dblinkName;
    }

    public void setDblinkName(String dblinkName) {
        this.dblinkName = dblinkName;
    }

}
