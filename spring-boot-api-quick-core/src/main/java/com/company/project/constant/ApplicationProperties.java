package com.company.project.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/***
 * 常用配置数据变量
 */
@Component
public class ApplicationProperties {

    public static String downloadPath;//下载资源保存地址

    public static String backUpDataFilePath;//数据库数据备份地址


    @Value("${custom.download.path}")
    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }


    @Value("${custom.backup.path}")
    public void setbackUpDataFilePath(String backUpDataFilePath) {
        this.backUpDataFilePath = backUpDataFilePath;
    }


}
