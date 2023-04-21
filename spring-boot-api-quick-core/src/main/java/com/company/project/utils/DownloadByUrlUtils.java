package com.company.project.utils;

import com.company.project.constant.ApplicationProperties;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/****
 * 文件资源下载工具
 */
public class DownloadByUrlUtils {

    /**
     * 通过url地址进行下载文件
     *
     * @param url      网页地址
     * @param fileName 文件名，不包含文件路径需要自己配置
     */
    public static boolean downloadByUrl(String url, String fileName, String downloadPath) {
        downloadPath = StringUtils.isEmpty(downloadPath) ? ApplicationProperties.downloadPath : downloadPath;
        String fullpath = downloadPath + "\\" + fileName;
        BufferedInputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        boolean result;
        try {
            URL path = new URL(url);
            inputStream = new BufferedInputStream(path.openStream());
            fileOutputStream = new FileOutputStream(fullpath);
            byte[] bytes = new byte[1024];//1m
            int len = 0;//为什么需要记录长度，便于在写入的时候确定长度
            while ((len = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, len);//将读取的文件进行写出
            }
            fileOutputStream.flush();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
