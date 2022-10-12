package com.company.project.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * 导出报表工具类
 */
public class ExportExcelUtil {

    private static final String XLS = ".xls";

    public static class CreateExcelDataVo {
        public int[] getMergeColumn() {
            return mergeColumn;
        }

        public void setMergeColumn(int[] mergeColumn) {
            this.mergeColumn = mergeColumn;
        }

        private int[] mergeColumn = null;

        private String title;//主标题

        private String sheetName;

        public String getSheetName() {
            return sheetName;
        }

        public void setSheetName(String sheetName) {
            this.sheetName = sheetName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String content[][];//内容，二维数组数据

        public String[][] getContent() {
            return content;
        }

        public void setContent(String[][] content) {
            this.content = content;
        }

        public ArrayList<String> getHeaders() {
            return headers;
        }

        public void setHeaders(ArrayList<String> headers) {
            this.headers = headers;
        }

        private ArrayList<String> headers;//列标题

    }


    /***
     * 合并特殊的列
     * @param sheet
     * @param column
     */
    public static void mergeSpecifiedColumn(HSSFSheet sheet, int column) {
        int totalRows = sheet.getLastRowNum(), firstRow = 0, lastRow = 0;
        boolean isLastCompareSame = false;//上一次比较是否相同
        //这里第一行是表头，从第三行开始判断是否相同
        if (totalRows >= 2) {
            for (int i = 2; i <= totalRows; i++) {
                String lastRowCellContent = sheet.getRow(i - 1).getCell(column).getStringCellValue();
                String curRowCellContent = sheet.getRow(i).getCell(column).getStringCellValue();
                if (curRowCellContent.equals(lastRowCellContent)) {
                    if (!isLastCompareSame) {
                        firstRow = i - 1;
                    }
                    lastRow = i;
                    isLastCompareSame = true;
                } else {
                    isLastCompareSame = false;
                    if (lastRow > firstRow) {
                        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, column, column));
                    }
                }
                //最后一行时判断是否有需要合并的行
                if ((i == totalRows) && (lastRow > firstRow)) {
                    sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, column, column));
                }
            }
        }
    }


    /**
     * @param title   标题
     * @param headers 表头
     * @param values  表中元素
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String title, String sheetName, List<String> headers, String[][] values, int[] mergeColumn) {
        //创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        //在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet hssfSheet = hssfWorkbook.createSheet(sheetName);
        //创建标题合并行
        hssfSheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) headers.size() - 1));
        //设置标题样式
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);   //设置居中样式
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置标题字体
        Font titleFont = hssfWorkbook.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        style.setFont(titleFont);
        //设置值表头样式 设置表头居中
        HSSFCellStyle hssfCellStyle = hssfWorkbook.createCellStyle();
        hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);   //设置居中样式
        hssfCellStyle.setBorderBottom(BorderStyle.THIN);
        hssfCellStyle.setBorderLeft(BorderStyle.THIN);
        hssfCellStyle.setBorderRight(BorderStyle.THIN);
        hssfCellStyle.setBorderTop(BorderStyle.THIN);
        //设置表内容样式
        //创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style1 = hssfWorkbook.createCellStyle();
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);
        style1.setAlignment(HorizontalAlignment.CENTER);
        style1.setVerticalAlignment(VerticalAlignment.CENTER);
        //产生标题行
        HSSFRow hssfRow = hssfSheet.createRow(0);
        hssfRow.setHeight((short) (30 * 20));
        HSSFCell cell = hssfRow.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style);
        //产生表头
        HSSFRow row1 = hssfSheet.createRow(1);
        for (int i = 0; i < headers.size(); i++) {
            HSSFCell hssfCell = row1.createCell(i);
            hssfCell.setCellValue(headers.get(i));
            hssfCell.setCellStyle(hssfCellStyle);
        }
        //创建标题合并行
        //  hssfSheet.addMergedRegion(new CellRangeAddress(0,(short)0,0,(short)headers.length - 1));
        //创建内容
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                row1 = hssfSheet.createRow(i + 2);
                for (int j = 0; j < values[i].length; j++) {
                    //将内容按顺序赋给对应列对象
                    HSSFCell hssfCell = row1.createCell(j);
                    hssfCell.setCellValue(values[i][j]);
                    hssfCell.setCellStyle(style1);
                }
            }
        }
        HSSFFont font = hssfWorkbook.createFont();
        font.setFontHeightInPoints((short) 15);//设置excel数据字体大小
        if (mergeColumn != null) {
            for (int i = 0; i < mergeColumn.length; i++) {
                mergeSpecifiedColumn(hssfSheet, mergeColumn[i]);
            }
        }
        hssfSheet.setHorizontallyCenter(true);
        return hssfWorkbook;
    }

    //发送响应流方法
    private static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /***
     *
     * @param createExcelDataVo
     * @return
     */
    public static HSSFWorkbook createHSSFWorkbook(ExportExcelUtil.CreateExcelDataVo createExcelDataVo) {
        if (StringUtils.isEmpty(createExcelDataVo.getTitle())) {
            throw new RuntimeException("title:不能为空");
        }
        if (StringUtils.isEmpty(createExcelDataVo.getSheetName())) {
            throw new RuntimeException("sheetName:不能为空");
        }
        if (CollectionUtils.isEmpty(createExcelDataVo.getHeaders())) {
            throw new RuntimeException("headers:不能为空");
        }
        HSSFWorkbook wb = getHSSFWorkbook(createExcelDataVo.getTitle(), createExcelDataVo.getSheetName(), createExcelDataVo.getHeaders(), createExcelDataVo.getContent(), createExcelDataVo.getMergeColumn());
        return wb;
    }


    /***
     *
     * @param wb
     * @param response
     * @param fileName
     */
    public static void createExcel(HSSFWorkbook wb, HttpServletResponse response, String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("fileName:不能为空");
        }
        if (fileName.toLowerCase().indexOf(XLS) == -1) {
            fileName = fileName + XLS;
        }
        try {
            setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}