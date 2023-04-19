package com.company.project.constant;

import com.company.project.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;

public class ApplicationProperties {

    public static String downloadPath;


    @Value("${custom.download.path}")
    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }


    public static String getDownloadPath() {
        return StringUtils.isEmpty(downloadPath) ? "D:\\quickDownload" : downloadPath;
    }

}
