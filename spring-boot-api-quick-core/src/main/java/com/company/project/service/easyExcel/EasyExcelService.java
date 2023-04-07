package com.company.project.service.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.company.project.master.vo.MoreWriteSheetVo;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/***
 *  EasyExcel的公用服務
 */
@Service
public class EasyExcelService {


    /***
     * 設置response基本的參數設置
     * @param response
     * @param name
     * @throws UnsupportedEncodingException
     */
    public void setExcelResponse(HttpServletResponse response, String name) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
    }


    /***
     * 獲取格式居中策略
     * @return
     */
    public HorizontalCellStyleStrategy getCenterHorizontalCellStyleStrategy() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return horizontalCellStyleStrategy;
    }

    /***
     *  通過模板生成
     * @param templatePath
     * @param fileName
     * @param list
     * @param otherData
     * @param response
     */
    public void oneWriteSheetTemplate(String templatePath, String fileName, Object list, Object otherData, HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        try {
            fileName = URLEncoder.encode(fileName + ".xlsx", "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        ClassPathResource classPathResource = new ClassPathResource(templatePath);
        try (InputStream inputStream = classPathResource.getInputStream();
             ServletOutputStream outputStream = response.getOutputStream()) {
            //设置输出流和模板信息
            ExcelWriter excelWriter = EasyExcel.write(outputStream).withTemplate(inputStream).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            //开启自动换行,自动换行表示每次写入一条list数据是都会重新生成一行空行,此选项默认是关闭的,需要提前设置为true
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            excelWriter.fill(list, fillConfig, writeSheet);
            excelWriter.fill(otherData, writeSheet);
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     *  通過模板生成
     * @param templatePath
     * @param fileName
     * @param response
     */
    public void moreWriteSheetTemplate(String templatePath, String fileName, List<MoreWriteSheetVo> moreWriteSheetVoList, HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        response.setHeader("content-type", "application/octet-stream");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        try {
            fileName = URLEncoder.encode(fileName + ".xlsx", "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        ClassPathResource classPathResource = new ClassPathResource(templatePath);
        try (InputStream inputStream = classPathResource.getInputStream();
             ServletOutputStream outputStream = response.getOutputStream()) {
            //设置输出流和模板信息
            ExcelWriter excelWriter = EasyExcel.write(outputStream).withTemplate(inputStream).build();
            int i = 0;
            for (MoreWriteSheetVo sheet : moreWriteSheetVoList) {
                WriteSheet writeSheet = EasyExcel.writerSheet(i).build();
                //开启自动换行,自动换行表示每次写入一条list数据是都会重新生成一行空行,此选项默认是关闭的,需要提前设置为true
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
                excelWriter.fill(sheet.getList(), fillConfig, writeSheet);
                excelWriter.fill(sheet.getOtherData(), writeSheet);
                i++;
            }
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
