package com.ysttench.application.auth.core.rdto;

import java.io.Serializable;

/**应用用户登录日志
 * @author gengyong
 *
 */
public class ApiLogUserLogin implements Serializable {
    
    private static final long serialVersionUID = -7479625353874702401L;
    private String userName;
    private String applicationKey;
    public ApiLogUserLogin() {}
    
    public ApiLogUserLogin(String userName, String applicationKey) {
        super();
        this.userName = userName;
        this.applicationKey = applicationKey;
    }

    public String getApplicationKey() {
        return applicationKey;
    }
    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    

}
