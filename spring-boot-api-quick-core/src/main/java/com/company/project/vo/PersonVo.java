package com.company.project.vo;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class PersonVo {

    @NotBlank(message = "姓名不能为空")
    private String personName;

    @NotBlank(message = "性别不能为空")
    private String gender;

    public String getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(String birthTime) {
        this.birthTime = birthTime;
    }

    @NotNull(message = "出生日期不能为空")
    private String birthTime;


    @NotBlank(message = "身份证号码不能为空")
    private String identityNumber;

    @NotBlank(message = "婚姻状况不能为空")
    private String maritalStatus;


    private String spouseName;

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    private String birthYear;



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


    private String curPageNum;

    public String getCurPageNum() {
        return curPageNum;
    }

    public void setCurPageNum(String curPageNum) {
        this.curPageNum = curPageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    private String pageSize;
}
