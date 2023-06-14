package com.company.project.test;

import com.spire.barcode.*;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.graphics.PdfImage;
import org.springframework.core.io.ClassPathResource;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class EIceblueTest {

    public static void main(String[] args) throws IOException {
        addBarCode();
    }

    public static byte[] addBarCode() throws IOException {
        ClassPathResource testPdf = new ClassPathResource("template/test.pdf");
        String fileName = testPdf.getPath();
        String newFileName = "D://newtest.pdf";
        PdfDocument pdf = new PdfDocument();
        pdf.loadFromFile(fileName);
        //获取第一页
        PdfPageBase page = pdf.getPages().get(0);
        //生成二维码图片，绘制到PDF页面
        BarcodeSettings settings = new BarcodeSettings();//创建二维码图形
        settings.setType(BarCodeType.QR_Code);
        settings.setData("http://www.baidu.com?param=123");
        settings.setShowText(false);
        settings.setShowTextOnBottom(true);
        settings.setQRCodeECL(QRCodeECL.Q);
        settings.setQRCodeDataMode(QRCodeDataMode.Numeric);
        BarCodeGenerator generator = new BarCodeGenerator(settings);
        Image image = generator.generateImage();
        PdfImage pdfImage = PdfImage.fromImage((BufferedImage) image);//绘制二维码图片到PDF
        page.getCanvas().drawImage(pdfImage, 40, 20);
        //保存PDF文档
        pdf.saveToFile(newFileName);
        pdf.dispose();
        // 调用上传方法 获取新的文件id
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(newFileName));
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] temp = new byte[1024];
        int size;
        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        in.close();
        out.close();
        return out.toByteArray();
    }
}
