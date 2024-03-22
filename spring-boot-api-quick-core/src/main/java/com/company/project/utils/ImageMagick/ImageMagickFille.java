package com.company.project.utils.ImageMagick;

import com.company.project.utils.FileUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.File;
import java.io.IOException;

/**
 * HEIC格式转换jpg
 */
public class ImageMagickFille {
    //下面用于检测 ImageMagick是否安装成功
    // convert --version
    private static final String IMAGE_MAGICK_PATH = "D:\\software\\ImageMagick-7.1.0-Q16-HDRI";
    public static ConvertCmd cmd;


    static {
        cmd = new ConvertCmd();
        //下面这句在linux不需要
        cmd.setSearchPath(IMAGE_MAGICK_PATH);
    }

    public static void main(String[] args) {
        File file = new File("D:\\temp\\heic\\b.heic");
        processIMGE(file);
    }

    private static byte[] processIMGE(File file) {
        IMOperation op = new IMOperation();
        String heicFilePath = file.getPath();
        String jpgFilePath = heicFilePath.replaceAll("\\.heic", "\\.jpg");
        op.addImage(heicFilePath);
        op.addImage(jpgFilePath);
        byte[] result = null;
        try {
            cmd.run(op);
            File jpgFile = new File(jpgFilePath);
            result = FileUtils.getBytesByFile(jpgFile);
            // jpgFile.deleteOnExit();
        } catch (IOException | InterruptedException | IM4JavaException e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }
}
