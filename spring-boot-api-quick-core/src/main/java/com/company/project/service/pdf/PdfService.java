package com.company.project.service.pdf;

import com.company.project.utils.PdfTableUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.Cell;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class PdfService {

    public void createTable(HttpServletResponse response) throws IOException, DocumentException {
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        // 打开文档
        document.open();

        createTitle(document);
        createHead(document);


      /*
        createBody(document);
        createFoot(document);*/
        document.close();
    }


    public PdfPCell createBorderCell(String name, Integer colspan) {
        PdfPCell cell;
        cell = new PdfPCell(new Paragraph(name, PdfTableUtils.messFont));
        cell.setHorizontalAlignment(Cell.ALIGN_LEFT);
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
        cell.setMinimumHeight(20);
        cell.setColspan(colspan);
        return cell;
    }


    public PdfPCell createNoBorderCell(String name, Integer colspan) {
        PdfPCell cell;
        cell = new PdfPCell(new Paragraph(name, PdfTableUtils.messFont));
        cell.setHorizontalAlignment(Cell.ALIGN_LEFT);
        cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
        cell.setMinimumHeight(20);
        cell.setColspan(colspan);
        cell.setBorder(0);
        return cell;
    }


    public void createTitle(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        // 设置宽度
        table.setWidthPercentage(100);
        // 设置间距
        table.setSpacingBefore(0);
        table.addCell(createNoBorderCell("AAAA", 1));
        table.addCell(createNoBorderCell("BBBB\n\n\n\n\n", 1));
        document.add(table);
    }

    public void createHead(Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        // 设置宽度
        table.setWidthPercentage(100);
        // 设置间距
        table.setSpacingBefore(0);
        PdfPCell pdfPCell1 = createBorderCell("CCCC", 1);
        pdfPCell1.setBackgroundColor(BaseColor.GRAY);
        table.addCell(pdfPCell1);
        PdfPCell pdfPCell2 = createBorderCell("DDDD", 3);
        table.addCell(pdfPCell2);
        document.add(table);


        PdfPTable table2 = new PdfPTable(4);
        // 设置宽度
        table2.setWidthPercentage(100);
        // 设置间距
        table2.setSpacingBefore(0);


        PdfPCell pdfPCell3 = createBorderCell("CCCC", 1);
        pdfPCell3.setBackgroundColor(BaseColor.GRAY);
        table2.addCell(pdfPCell3);
        PdfPCell pdfPCell4 = createBorderCell("DDDD", 1);
        table2.addCell(pdfPCell4);


        PdfPCell pdfPCell5 = createBorderCell("CCCC", 1);
        pdfPCell5.setBackgroundColor(BaseColor.GRAY);
        table2.addCell(pdfPCell5);
        PdfPCell pdfPCell6 = createBorderCell("DDDD", 1);
        table2.addCell(pdfPCell6);


        document.add(table2);


    }

    public void createBody(Document document) {

    }

    public void createFoot(Document document) {

    }
}
