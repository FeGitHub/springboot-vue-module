package com.company.project.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author LWB
 * @Description
 */
public class FileTool {

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
}
