package com.company.project.utils.qrcode;

import java.awt.image.BufferedImage;

public class OrCodeImage {

    static int splitColor = 0xFF7f7f7f;

    public static BufferedImage blackAndWhite(BufferedImage image) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) { //读取每个像素点颜色
                //System.out.print(image.getRGB(x, y));
                if (image.getRGB(x, y) > splitColor) { //我以0xFF700000 颜色为分界线，比这黑当作黑，比这个白当作白
                    image.setRGB(x, y, 0xFFFFFFFF); // = 白
                } else {
                    image.setRGB(x, y, 0xFF000000); // = 黑
                }
            }
        }

        return image;

    }

}
