package com.company.project.vo.test;

import java.util.List;

public class TestParentVo {

    private String testParentStr;

    private boolean testParentBoolean;

    public List<TestChildVo> getTestChildVoList() {
        return TestChildVoList;
    }

    public void setTestChildVoList(List<TestChildVo> testChildVoList) {
        TestChildVoList = testChildVoList;
    }

    List<TestChildVo> TestChildVoList;


    public String getTestParentStr() {
        return testParentStr;
    }

    public void setTestParentStr(String testParentStr) {
        this.testParentStr = testParentStr;
    }

    public boolean isTestParentBoolean() {
        return testParentBoolean;
    }

    public void setTestParentBoolean(boolean testParentBoolean) {
        this.testParentBoolean = testParentBoolean;
    }
}
