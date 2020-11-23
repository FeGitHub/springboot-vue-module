package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "ERROR_LOG")
public class ErrorLog {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "REQUEST_URL")
    private String requestUrl;

    @Column(name = "CREATE_TIME")
    private Date createTime;



    @Column(name = "ERROR_INFO")
    private String errorInfo;

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



    /**
     * @return ERROR_INFO
     */
    public String getErrorInfo() {
        return errorInfo;
    }

    /**
     * @param errorInfo
     */
    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}