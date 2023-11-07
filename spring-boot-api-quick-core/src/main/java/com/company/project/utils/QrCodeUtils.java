package com.company.project.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/****
 * 二维码工具类
 */
public class QrCodeUtils {

    public static void main(String[] args) {
        String base64Url = getQrCodeJumpBase64("https://app.gov.mo/vi-dev/1/?key=1&data=x01|x02|x03|x04|x05|x06|x07|x08|x09|x10|x11&sign=MEQCIDiRN7v_sr9sJQG_2d7QlV4Es9aiHOvXtZih24VjGO9ZAiAW5wk4KUBMVqPShrdoJkSmWIV85I4eT7nK6gVp3NVvvQ==");
        System.out.println(base64Url);
        System.out.println(getQrCodeUrl(base64Url));
    }

    /****
     * 將url轉換為二維碼數據的base64字符串
     * @param url
     * @return
     */
    public static String getQrCodeJumpBase64(String url) {
        String base64Str = "";
        int width = 300;
        int height = 300;
        // 定义二维码的参数
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        // 定义字符集编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 纠错的等级 L > M > Q > H 纠错的能力越高可存储的越少，一般使用M
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 设置图片边距
        hints.put(EncodeHintType.MARGIN, 2);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 最终生成 参数列表 （1.内容 2.格式 3.宽度 4.高度 5.二维码参数）
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image = toBufferedImage(bitMatrix);
            ImageIO.write(image, "png", out);
            byte[] bytes = out.toByteArray();
            if (StringUtils.notEmpty(bytes)) {
                base64Str = Base64.encodeBase64String(bytes);
            }
            return base64Str;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /****
     * 將url轉換為二維碼數據的base64字符串
     * @param url
     * @return
     */
    public static byte[] getQrCodeToByte(String url) {
        int width = 300;
        int height = 300;
        // 定义二维码的参数
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        // 定义字符集编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 纠错的等级 L > M > Q > H 纠错的能力越高可存储的越少，一般使用M
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        // 设置图片边距
        hints.put(EncodeHintType.MARGIN, 2);
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 最终生成 参数列表 （1.内容 2.格式 3.宽度 4.高度 5.二维码参数）
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image = toBufferedImage(bitMatrix);
            ImageIO.write(image, "png", out);
            byte[] bytes = out.toByteArray();
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    /****
     * 从二维码中获取信息
     * @param base64Image
     * @return
     */
    public static String getQrCodeUrl(String base64Image) {
        byte[] imageBytes = Base64.decodeBase64(base64Image);
        return getQrCodeUrl(imageBytes);
    }


    /****
     * 从二维码中获取信息
     * @param imageBytes
     * @return
     */
    public static String getQrCodeUrl(byte[] imageBytes) {
        String qrCodeContent = "";
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(bis);
            bis.close();
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap);
            qrCodeContent = result.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qrCodeContent;
    }


}
