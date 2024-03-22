package com.test.qrcode;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestBarCode {
    public static void main(String[] args) {

        QRReader();
    }

    public static void QRReader() {
        try {
            File file = new File("E:\\output\\B.png");
            BufferedImage image = ImageIO.read(file);
            ImageLuminanceSource source = new ImageLuminanceSource(ORCodeImageUtil.blackAndWhite(image), image.getWidth(), image.getHeight()); //图像处理之后 在传入
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            QRCodeReader reader = new QRCodeReader();

            Map<DecodeHintType, Object> hints = new LinkedHashMap<DecodeHintType, Object>();
            // 解码设置编码方式为：utf-8，
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            //优化精度
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            //复杂模式，开启PURE_BARCODE模式
            //hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
            Result result = reader.decode(binaryBitmap, null);
            String text = result.getText();
            System.out.println("解析的结果: " + result.getText());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (NotFoundException e) {
            System.out.println("二维码没找到");
        } catch (FormatException e) {
            System.out.println("二维码无法解析");

        } catch (ChecksumException e) {
            System.out.println("二维码纠错失败");
            System.out.println(e.toString());
        }

    }

}
