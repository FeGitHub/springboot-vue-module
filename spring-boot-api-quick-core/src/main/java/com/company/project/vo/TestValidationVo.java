package com.company.project.vo;

import com.company.project.validate.rule.DateTimeStr;
import com.company.project.vo.base.RequestVo;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TestValidationVo implements RequestVo {


    @NotBlank
    @DateTimeStr(format = "yyyy-MM-dd", message = "日期格式和yyyy-MM-dd不符合")
    private String testDateStr;


    @NotBlank
    private String testStr;

}
