package com.company.project.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/***
 *  创建临时文件，使用完之后删除
 */
public class InputStreamToFileConverter {
    public static File convert(InputStream inputStream) throws IOException {
        File tempFile = File.createTempFile("temp", ".tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }
}
