package com.company.project.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.company.project.service.easyExcel.ExcelRowMerge;
import com.company.project.service.easyExcel.ExcelRowMergeKey;
import lombok.Data;

import java.util.Date;

@Data

public class TestMergeExcelVO {


    /**
     * 订单名称
     */
    @ExcelRowMerge
    @ColumnWidth(40)
    @ExcelProperty("订单名称")
    private String orderName;

    /**
     * 订单号
     */
    @ExcelRowMergeKey
    @ExcelRowMerge
    @ColumnWidth(40)
    @ExcelProperty("订单号122")
    private String orderNo;


    /**
     * 创建时间
     */
    @ExcelRowMerge
    @ColumnWidth(40)
    @ExcelProperty("创建时间")
    private Date orderCreateTime;


}
