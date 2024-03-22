package com.company.project.utils.qrcode;

import com.google.zxing.LuminanceSource;

import java.awt.image.BufferedImage;

public class ImageLuminanceSource extends LuminanceSource {

    private final BufferedImage image;


    public ImageLuminanceSource(BufferedImage image, int width, int height) {
        super(width, height);
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY); //图像要变成灰度的，因为getRow这个方法取的值必须是0-255之间
        this.image.getGraphics().drawImage(image, 0, 0, null);
    }


    //要求是返回一个以行为主的数组
    @Override
    public byte[] getMatrix() { //
        // TODO Auto-generated method stub
        int width = getWidth();
        int height = getHeight();
        int area = width * height;
        byte[] matrix = new byte[area];
        image.getRaster().getDataElements(0, 0, width, height, matrix);
        return matrix;
    }


    //要求是返回图片中某一行的亮度数据
    @Override
    public byte[] getRow(int y, byte[] row) {
        //官方的注释
        //从底层平台的位图中获取一行亮度数据。取值范围为0(黑色)到255(白色)。因为Java没有unsigned字节类型，所以调用者必须按位计算，并且每个值都使用0xff。
        //这个方法的实现最好是只获取这一行而不是整个图像，因为不可能安装2D reader，也不可能调用getMatrix()。
        //y - The row to fetch, which must be in [0,getHeight())
        //row - An optional preallocated array. If null or too small, it will be ignored. Always use the returned object, and ignore the .length of the array.
        if (y < 0 || y >= getHeight()) {
            throw new IllegalArgumentException("不在图片的高度范围之内: " + y);
        }
        int width = getWidth();
        if (row == null || row.length < width) {
            row = new byte[width];
        }
        image.getRaster().getDataElements(0, 0 + y, width, 1, row);//返回图片规定矩形的像素数据，然后getRow需要的是一行的，所以高度是1。所以整句话的意思是从标点 0,y开始读取一像素高度的矩形，这不就是一行吗
        return row;

    }


}


