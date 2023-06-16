package com.company.project.service.easyExcel;

import com.company.project.vo.TestMergeExcelVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TestEasyExcelDataService {

    public List<TestMergeExcelVO> getOrderExportVO() {
        List<TestMergeExcelVO> orderExportVOS = new ArrayList<>();
        Date orderDetailCreateTime = new Date();
        // 模拟查询出订单导出数据
        TestMergeExcelVO orderExportVO1 = new TestMergeExcelVO();
        orderExportVO1.setOrderNo("A");
        orderExportVO1.setOrderName("SS");
        orderExportVO1.setOrderCreateTime(orderDetailCreateTime);
        orderExportVOS.add(orderExportVO1);


        TestMergeExcelVO orderExportVO12 = new TestMergeExcelVO();
        orderExportVO12.setOrderNo("A");
        orderExportVO12.setOrderName("SS");
        orderExportVO12.setOrderCreateTime(orderDetailCreateTime);
        orderExportVOS.add(orderExportVO12);


        TestMergeExcelVO orderExportVO2 = new TestMergeExcelVO();
        orderExportVO2.setOrderNo("A1");
        orderExportVO2.setOrderName("SS");
        orderExportVO2.setOrderCreateTime(orderDetailCreateTime);
        orderExportVOS.add(orderExportVO2);

        TestMergeExcelVO orderExportVO21 = new TestMergeExcelVO();
        orderExportVO21.setOrderNo("A1");
        orderExportVO21.setOrderName("SS11");
        orderExportVO21.setOrderCreateTime(orderDetailCreateTime);
        orderExportVOS.add(orderExportVO21);


        TestMergeExcelVO orderExportVO3 = new TestMergeExcelVO();
        orderExportVO3.setOrderNo("B");
        orderExportVO3.setOrderName("SS");
        orderExportVO3.setOrderCreateTime(orderDetailCreateTime);
        orderExportVOS.add(orderExportVO3);

        TestMergeExcelVO orderExportVO4 = new TestMergeExcelVO();
        orderExportVO4.setOrderNo("B");
        orderExportVO4.setOrderName("SS");
        orderExportVO4.setOrderCreateTime(orderDetailCreateTime);
        orderExportVOS.add(orderExportVO4);
        return orderExportVOS;
    }
}


