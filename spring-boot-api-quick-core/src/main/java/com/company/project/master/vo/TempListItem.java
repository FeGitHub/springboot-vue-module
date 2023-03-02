package com.company.project.master.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TempListItem {
    @ExcelProperty("姓名")
    private String name;
    @ExcelProperty("数字")
    private Integer number;
}
