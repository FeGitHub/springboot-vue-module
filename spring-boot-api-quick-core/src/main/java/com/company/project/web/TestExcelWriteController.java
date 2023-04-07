package com.company.project.web;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.company.project.master.vo.MoreWriteSheetVo;
import com.company.project.master.vo.TempListItem;
import com.company.project.master.vo.TempMapData;
import com.company.project.master.vo.UserEntity1;
import com.company.project.service.easyExcel.EasyExcelService;
import com.company.project.service.test.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/excel")
public class TestExcelWriteController {
    @Autowired
    private ExcelService excelService;

    @Autowired
    private EasyExcelService easyExcelService;


    /***
     * 根據模板生成數據
     * @param response
     * @throws IOException
     */
    // http://localhost:8085/excel/oneWriteSheetTemplate
    @RequestMapping(value = "/oneWriteSheetTemplate")
    public void oneWriteSheetTemplate(HttpServletResponse response) {
        //用于放置离散数据
        TempMapData tempMapData = new TempMapData();
        tempMapData.setYear("2023");
        //用于放置列表数据
        List<TempListItem> list = new ArrayList<>();
        TempListItem tempListItem = new TempListItem();
        tempListItem.setName("胡萝北");
        tempListItem.setNumber(10);
        list.add(tempListItem);
        TempListItem demo11 = new TempListItem();
        demo11.setName("希红事");
        demo11.setNumber(30);
        list.add(demo11);
        easyExcelService.oneWriteSheetTemplate("template/oneWriteSheetTemplate.xlsx", "test", list, tempMapData, response);
    }


    /***
     * 根據模板生成數據
     * @param response
     * @throws IOException
     */
    // http://localhost:8085/excel/moreWriteSheetTemplate
    @RequestMapping(value = "/moreWriteSheetTemplate")
    public void moreWriteSheetTemplate(HttpServletResponse response) {
        //用于放置离散数据
        TempMapData tempMapData = new TempMapData();
        tempMapData.setYear("2023");
        //用于放置列表数据
        List<TempListItem> list = new ArrayList<>();
        TempListItem tempListItem = new TempListItem();
        tempListItem.setName("胡萝北");
        tempListItem.setNumber(10);
        list.add(tempListItem);
        TempListItem demo11 = new TempListItem();
        demo11.setName("希红事");
        demo11.setNumber(30);
        list.add(demo11);
        List<MoreWriteSheetVo> moreWriteSheetVoList = new ArrayList<>();
        moreWriteSheetVoList.add(new MoreWriteSheetVo(list, tempMapData));
        moreWriteSheetVoList.add(new MoreWriteSheetVo(list, tempMapData));
        easyExcelService.moreWriteSheetTemplate("template/moreWriteSheetTemplate.xlsx", "moreWriteSheetTemplate", moreWriteSheetVoList, response);
    }


    /**
     * 根據實體vo導出excel
     *
     * @param response
     * @throws IOException
     */
    // http://localhost:8085/excel/downloadByVo
    @GetMapping("/downloadByVo")
    public void downloadByVo(HttpServletResponse response) throws IOException {
        // 这里注意使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        ExcelWriter excelWriter = null;
        try {
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = easyExcelService.getCenterHorizontalCellStyleStrategy();
            // 这里 指定文件
            excelWriter = EasyExcel.write(response.getOutputStream(), UserEntity1.class).build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
            for (int i = 0; i < 5; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class 实际上可以一直变
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).registerWriteHandler(horizontalCellStyleStrategy).head(UserEntity1.class).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<UserEntity1> data = excelService.getData();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }


/*    @RequestMapping("/testExcel")
    @ResponseBody
    public void testExcel(@RequestParam("excelInput") MultipartFile excelInput) throws IOException {
        Workbook wb = excelService.createWorkbook(excelInput.getInputStream());
        // 2.读取页脚sheet
        Sheet sheetAt = wb.getSheetAt(0);
        // 3.循环读取某一行
        for (Row row : sheetAt) {
            // 4.读取每一行的单元格
            String stringCellValue = row.getCell(0).getStringCellValue(); // 第一列数据
            System.out.println(stringCellValue);
        }
    }*/


}
