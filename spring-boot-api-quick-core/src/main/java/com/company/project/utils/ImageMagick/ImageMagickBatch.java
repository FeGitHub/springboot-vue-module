package com.company.project.utils.ImageMagick;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * HEIC格式转换jpg
 */
public class ImageMagickBatch {

    private static final String IMAGE_MAGICK_PATH = "D:\\software\\ImageMagick-7.1.0-Q16-HDRI";
    public static ConvertCmd cmd = null;
    static String path = "D:\\temp\\heic\\";
    static String path1 = "D:\\temp\\jpg\\";

    static {
        cmd = new ConvertCmd();
        //下面这句在linux不需要
        cmd.setSearchPath(IMAGE_MAGICK_PATH);
    }

    public static void main(String[] args) {
        File file = new File(path);
        File[] files = file.listFiles();
        long startTime = System.currentTimeMillis();
        System.out.println(startTime);
        Arrays.stream(files).forEach(ff -> {
            processIMGE(ff);
        });
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }

    private static void processIMGE(File file) {
        IMOperation op = new IMOperation();
        String fileName = file.getName();
        String destName = fileName.substring(0, fileName.indexOf("."));
        op.addImage(path + fileName);
        op.addImage(path1 + destName + ".jpg");
        try {
            cmd.run(op);
        } catch (IOException | InterruptedException | IM4JavaException e) {
            e.printStackTrace();
        } finally {
            op = null;
        }
    }
}
