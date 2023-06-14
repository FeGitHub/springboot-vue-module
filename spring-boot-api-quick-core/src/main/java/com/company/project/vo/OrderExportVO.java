package com.company.project.vo;


import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.company.project.service.easyExcel.ExcelKey;
import com.company.project.service.easyExcel.ExcelMerge;
import lombok.Data;

import java.util.Date;

/**
 * @author ggBall
 * @version 1.0.0
 * @ClassName OrderExportVO.java
 * @Description 导出订单明细
 * @createTime 2023年03月01日 14:28:00
 */
@Data

public class OrderExportVO {

    /**
     * 订单号
     */
    @ExcelKey
    @ExcelMerge
    @ColumnWidth(40)
    private String orderNo;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 创建时间
     */
    @ExcelMerge
    private Date orderCreateTime;


    /**
     * 订单明细号
     */
    private String orderDetailNo;

    /**
     * 订单明细名称
     */
    private String orderDetailName;

    /**
     * 创建时间
     */
    private Date orderDetailCreateTime;

}
