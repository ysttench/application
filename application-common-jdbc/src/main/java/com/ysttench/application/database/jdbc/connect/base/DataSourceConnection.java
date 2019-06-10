package com.ysttench.application.database.jdbc.connect.base;

import java.util.HashMap;
import java.util.List;

public interface DataSourceConnection {
    
    public <T> List<T> readMetaData(ConnectInfo connectInfo, HashMap<String, String> param) throws Exception;
    
    public boolean testConnection(ConnectInfo connectInfo);

}
