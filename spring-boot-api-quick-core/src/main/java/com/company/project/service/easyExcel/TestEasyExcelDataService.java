package com.company.project.service.easyExcel;

import com.company.project.vo.OrderExportVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TestEasyExcelDataService {

    public List<OrderExportVO> getOrderExportVO() {
        List<OrderExportVO> orderExportVOS = new ArrayList<>();
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
        return orderExportVOS;
    }
}


