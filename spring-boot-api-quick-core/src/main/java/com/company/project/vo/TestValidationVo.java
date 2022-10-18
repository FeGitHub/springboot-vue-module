package com.company.project.vo;

import com.company.project.validate.rule.DateTimeStr;
import com.company.project.vo.base.RequestVo;

import javax.validation.constraints.NotBlank;

public class TestValidationVo implements RequestVo {


    /**
     * 生效日期。格式為yyyy-MM-dd
     */
    @NotBlank
    @DateTimeStr(format = "yyyy-MM-dd", message = "日期格式和yyyy-MM-dd不符合")
    private String testDateStr;


    public String getTestDateStr() {
        return testDateStr;
    }

    public void setTestDateStr(String testDateStr) {
        this.testDateStr = testDateStr;
    }
}
