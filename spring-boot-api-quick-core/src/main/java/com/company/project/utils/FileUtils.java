package com.company.project.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {

    /**
     * <p>
     * 创建文件
     * </p>
     *
     * @param path
     * @param fileName
     * @return
     * @throws Exception
     */
    public static File createFile(String path, String fileName) throws Exception {
        try {
            // 目录
            File file = new File(path);
            // 判断文件夹/文件 是否存在
            if (!file.exists()) {
                // 创建目录
                file.mkdirs();
            }
            // 文件
            String filePath = path + fileName;
            Date now = new Date();
            SimpleDateFormat f = new SimpleDateFormat("今天是" + "yyyy年MM月dd日 E kk点mm分ss秒");
            String suffix = f.format(now);
            file = new File(filePath + suffix + ".txt");
            // 判断文件夹/文件 是否存在
            if (!file.exists()) {
                // 如果不存在
                // 创建文件
                file.createNewFile();
            } else {
                // 如果已存在，创建一个新的文件
                file = new File(filePath);
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
            System.err.println("创建文件异常");
            throw e;
        }
    }

    /**
     * <创建文件>
     * 如果对应的文件夹不存在就创建
     *
     * @param
     * @param
     * @return
     * @throws Exception
     */
    public static File createFileFolder(String filePath) throws Exception {
        try {
            // 目录
            File file = new File(filePath);
            // 判断文件夹/文件 是否存在
            if (file.exists()) {
                return file;
            }
            File parentFile = file.getParentFile();
            //判断文件夹是否存在
            if (!parentFile.exists()) {
                //如果不存在创建目录
                parentFile.mkdirs();
            }
            //创建文件
            file.createNewFile();
            return file;
        } catch (Exception e) {
            System.err.println("创建文件异常");
            throw e;
        }
    }


    /***
     * 判断队对应的文件夹是否存在，不存在就创建
     * @param fileFolder
     */
    public static boolean makeFileFolderExists(String fileFolder) {
        File file = new File(fileFolder);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
            return false;
        }
        return true;
    }


    /***
     * 获取文件后缀名
     * @param fileName
     * @return
     */
    public static String getSuffixName(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * <p>
     * 追加形式向文件写入内容
     * </p>
     *
     * @param filePath
     * @param content
     * @return
     * @throws Exception
     */
    public static void writeFileContent(String filePath, String content) throws Exception {
        FileOutputStream fileOutStream = null;
        PrintWriter printWriter = null;
        try {
            // true 表示以追加的形式写入内容
            fileOutStream = new FileOutputStream(filePath, true);
            printWriter = new PrintWriter(fileOutStream);
            printWriter.write(content.toCharArray());
            printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("向文件写入内容异常");
            throw e;
        } finally {
            try {
                if (null != fileOutStream) {
                    fileOutStream.close();
                }
                if (null != printWriter) {
                    printWriter.close();
                }
            } catch (Exception e2) {
                System.out.println("文件流关闭失败");
            }
        }
    }

    /**
     * <写入文件>
     *
     * @param filePath
     * @param input
     * @throws Exception
     */
    public static void writeFile(String filePath, InputStream input) throws Exception {
        OutputStream outputStream = null;
        try {
            //先创建文件
            File file = createFileFolder(filePath);
            outputStream = new FileOutputStream(file);
            //inputStream 转 outputStream
            int bytesWritten = 0;
            int byteCount = 0;
            byte[] bytes = new byte[1024];
            while ((byteCount = input.read(bytes)) != -1) {
                outputStream.write(bytes, bytesWritten, byteCount);
                bytesWritten += byteCount;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            //关闭流
            if (null != input) {
                input.close();
            }
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    /**
     * 创建ResponseEntity<byte[]>对象,用于下载字节数组文件
     *
     * @param req      请求
     * @param data     文件字节数组
     * @param fileName 下载时显示的文件名
     * @return
     */
    public static ResponseEntity<byte[]> createResEntity(HttpServletRequest req, byte[] data, String fileName) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
        //设置响应方式为二进制，以二进制流传输
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
    }


    public static void writeFileToResponse(HttpServletResponse response, byte[] content) {
        ByteArrayInputStream in;
        OutputStream out;
        try {
            in = new ByteArrayInputStream(content);
            out = response.getOutputStream();
            byte[] b = new byte[content.length];
            while ((in.read(b)) != -1) {
                out.write(b);
                out.flush();
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
