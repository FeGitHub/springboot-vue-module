package com.company.project.model;

import java.util.Date;
import javax.persistence.*;

public class Person {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="SEQ_FW_DEMO_GENERATOR")//定义主键的生成策略
    @SequenceGenerator(name="SEQ_FW_DEMO_GENERATOR", sequenceName="SEQ_FW_PERSON")//采用的序列方案
    private String id;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "LAST_UPDATE_TIME")
    private Date lastUpdateTime;

    @Column(name = "VERSION_NUMBER")
    private Short versionNumber;

    @Column(name = "PERSON_NAME")
    private String personName;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "BIRTH_TIME")
    private Date birthTime;

    @Column(name = "IDENTITY_NUMBER")
    private String identityNumber;

    @Column(name = "MARITAL_STATUS")
    private String maritalStatus;

    @Column(name = "SPOUSE_NAME")
    private String spouseName;

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
     * @return LAST_UPDATE_TIME
     */
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime
     */
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return VERSION_NUMBER
     */
    public Short getVersionNumber() {
        return versionNumber;
    }

    /**
     * @param versionNumber
     */
    public void setVersionNumber(Short versionNumber) {
        this.versionNumber = versionNumber;
    }

    /**
     * @return PERSON_NAME
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * @param personName
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    /**
     * @return GENDER
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return BIRTH_TIME
     */
    public Date getBirthTime() {
        return birthTime;
    }

    /**
     * @param birthTime
     */
    public void setBirthTime(Date birthTime) {
        this.birthTime = birthTime;
    }

    /**
     * @return IDENTITY_NUMBER
     */
    public String getIdentityNumber() {
        return identityNumber;
    }

    /**
     * @param identityNumber
     */
    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    /**
     * @return MARITAL_STATUS
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * @param maritalStatus
     */
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * @return SPOUSE_NAME
     */
    public String getSpouseName() {
        return spouseName;
    }

    /**
     * @param spouseName
     */
    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }
}