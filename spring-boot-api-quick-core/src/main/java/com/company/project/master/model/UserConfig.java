package com.company.project.master.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "user_config")
public class UserConfig {
    @Id
    private String id;

    @Column(name = "user_login")
    private String userLogin;

    @Column(name = "user_token")
    private String userToken;

    @Column(name = "user_remark")
    private String userRemark;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return user_login
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * @param userLogin
     */
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
     * @return user_token
     */
    public String getUserToken() {
        return userToken;
    }

    /**
     * @param userToken
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    /**
     * @return user_remark
     */
    public String getUserRemark() {
        return userRemark;
    }

    /**
     * @param userRemark
     */
    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }
}
