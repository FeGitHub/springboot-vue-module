package com.company.project.utils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/****
 * 压缩文件工具
 */
public class ZipUtils {

    /***
     * 压缩包下载
     * @param zipName
     * @param downloadFileDtoList
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     */
    public static ResponseEntity downloadZip(String zipName, List<DownloadFileVo> downloadFileDtoList, HttpServletResponse response) throws UnsupportedEncodingException {
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(zipName.getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
        byte[] dataByteArr = new byte[0];
        if (CollectionUtils.isNotEmpty(downloadFileDtoList)) {
            try {
                dataByteArr = ZipUtils.zipFile(downloadFileDtoList);
                response.getOutputStream().write(dataByteArr);
                response.flushBuffer();
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity(dataByteArr, HttpStatus.OK);
    }


    /***
     * 压缩多文件
     * @param downloadFileDtoList
     * @return
     */
    public static byte[] zipFile(List<DownloadFileVo> downloadFileDtoList) {
        // 将字节写到一个字节输出流里
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream out = new ZipOutputStream(baos)) {
            // 创建zip file in memory
            for (DownloadFileVo downloadFileDto : downloadFileDtoList) {
                ZipEntry entry = new ZipEntry(downloadFileDto.getFileName());
                entry.setSize(downloadFileDto.getByteDataArr().length);
                out.putNextEntry(entry);
                out.write(downloadFileDto.getByteDataArr());
                out.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException("压缩zip包出现异常");
        }
        return baos.toByteArray();
    }


    /***
     * 文件下载vo
     */
    public static class DownloadFileVo implements Serializable {

        private static final long serialVersionUID = 2648469408255550980L;

        // 存放文件名
        private String fileName = "";

        // 存放文件字节流
        private byte[] byteDataArr = new byte[0];

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public byte[] getByteDataArr() {
            return byteDataArr;
        }

        public void setByteDataArr(byte[] byteDataArr) {
            this.byteDataArr = byteDataArr;
        }

    }


}
