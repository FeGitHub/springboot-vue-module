package com.company.project.utils;


import javax.imageio.ImageIO;
import java.io.File;
import java.util.Arrays;

public class HEICToJPGConverter {

    public static void main(String[] args) {
        try {
            // 读取HEIC文件
            File inputFile = new File("D:\\temp\\heic\\b.heic");
            String[] readerFormatNames = ImageIO.getReaderFormatNames();
            System.out.println("reader: " + Arrays.asList(readerFormatNames));

       /*     BufferedImage image = ImageIO.read(inputFile);
            // 转换为JPG格式
            File outputFile = new File("output.jpg");
            ImageIO.write(image, "jpg", outputFile);*/

            System.out.println("转换完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
