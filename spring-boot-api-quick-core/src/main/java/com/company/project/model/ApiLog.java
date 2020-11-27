package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "API_LOG")
public class ApiLog {
    @Id
    @Column(name = "ID")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "REQUEST_IP")
    private String requestIp;

    @Column(name = "REQUEST_URL")
    private String requestUrl;

    @Column(name = "REQUEST_PARAMS")
    private String requestParams;

    @Column(name = "CREATE_TIME")
    private Date createTime;


    @Column(name = "TOKEN")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return ID
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
     * @return REQUEST_IP
     */
    public String getRequestIp() {
        return requestIp;
    }

    /**
     * @param requestIp
     */
    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    /**
     * @return REQUEST_URL
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * @param requestUrl
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    /**
     * @return REQUEST_PARAMS
     */
    public String getRequestParams() {
        return requestParams;
    }

    /**
     * @param requestParams
     */
    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    /**
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}