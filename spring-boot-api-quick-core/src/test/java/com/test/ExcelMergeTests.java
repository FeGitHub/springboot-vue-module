package com.test;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.company.project.service.easyExcel.EasyExcelService;
import com.company.project.service.easyExcel.MyMergeStrategy;
import com.company.project.vo.OrderExportVO;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelMergeTests {

    private List<OrderExportVO> orderExportVOS;




   /* @Before
    public void setData() {
        orderExportVOS = new ArrayList<>();
        // 模拟查询出订单导出数据
        for (int i = 0; i < 20; i++) {
            Date orderCreateTime = new Date();
            String orderNo = "orderNo" + i;
            String orderName = "orderName" + i;
            Random random = new Random();
            int r = random.nextInt(5);
            for (int j = 0; j < r; j++) {
                Date orderDetailCreateTime = new Date();
                String orderDetailNo = "orderDetailNo" + i;
                String orderDetailName = "orderDetailName" + i;
                OrderExportVO orderExportVO = new OrderExportVO();
                orderExportVO.setOrderNo(orderNo);
                orderExportVO.setOrderName(orderName);
                orderExportVO.setOrderCreateTime(orderCreateTime);
                orderExportVO.setOrderDetailNo(orderDetailNo);
                orderExportVO.setOrderDetailName(orderDetailName);
                orderExportVO.setOrderDetailCreateTime(orderDetailCreateTime);
                orderExportVOS.add(orderExportVO);
            }
        }
    } */

    @Before
    public void setData() {
        orderExportVOS = new ArrayList<>();
        Date orderDetailCreateTime = new Date();
        // 模拟查询出订单导出数据
        OrderExportVO orderExportVO1 = new OrderExportVO();
        orderExportVO1.setOrderNo("A");
        orderExportVO1.setOrderName("SS");
        orderExportVO1.setOrderCreateTime(orderDetailCreateTime);
        orderExportVOS.add(orderExportVO1);

        OrderExportVO orderExportVO2 = new OrderExportVO();
        orderExportVO2.setOrderNo("A");
        orderExportVO2.setOrderName("SS");
        orderExportVO2.setOrderCreateTime(orderDetailCreateTime);
        orderExportVOS.add(orderExportVO2);


        OrderExportVO orderExportVO3 = new OrderExportVO();
        orderExportVO3.setOrderNo("B");
        orderExportVO3.setOrderName("SS");
        orderExportVO3.setOrderCreateTime(orderDetailCreateTime);
        orderExportVOS.add(orderExportVO3);

        OrderExportVO orderExportVO4 = new OrderExportVO();
        orderExportVO4.setOrderNo("B");
        orderExportVO4.setOrderName("SS");
        orderExportVO4.setOrderCreateTime(orderDetailCreateTime);
        orderExportVOS.add(orderExportVO4);


    }


    @Test
    public void testMergeWrite() throws IllegalAccessException {
        //  自定义合并单元格策略
        String fileName = "D://註解合併.xls";
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = EasyExcelService.getCenterHorizontalCellStyleStrategy();
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, OrderExportVO.class)
                .registerWriteHandler(new MyMergeStrategy<>(orderExportVOS))
                .sheet("模板").registerWriteHandler(horizontalCellStyleStrategy).doWrite(orderExportVOS);
    }

}
