package com.company.project.slave.model;

import javax.persistence.*;

@Table(name = "TEST_TABLE")
public class TestTable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "TEST_NAME")
    private String testName;

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
     * @return TEST_NAME
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
}