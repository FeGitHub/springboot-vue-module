package com.company.project.slave.model;

import javax.persistence.*;

@Table(name = "TEST_TABLE")
public class TestTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "test_name")
    private String testName;

    @Column(name = "test_remark")
    private String testRemark;

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
     * @return test_name
     */
    public String getTestName() {
        return testName;
    }

    /**
     * @param testName
     */
    public void setTestName(String testName) {
        this.testName = testName;
    }

    /**
     * @return test_remark
     */
    public String getTestRemark() {
        return testRemark;
    }

    /**
     * @param testRemark
     */
    public void setTestRemark(String testRemark) {
        this.testRemark = testRemark;
    }
}