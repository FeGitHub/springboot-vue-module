package com.company.project.service.download;

import com.company.project.constant.ApplicationProperties;
import com.company.project.utils.DateUtils;
import com.company.project.utils.DownloadByUrlUtils;
import com.company.project.utils.FileUtils;
import com.company.project.utils.UuidUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/***
 * 文件资源下载服务
 */
@Service
public class DownloadService {

    // http://blog.luckly-mjw.cn/tool-show/m3u8-downloader/index.html  m3u8 视频在线提取工具

    private Logger logger = LoggerFactory.getLogger(DownloadService.class);

    private static int limitDownloadMaxSize = 200;//最大下载文件数

    public void getPictureAndVideoByUrl(String url) throws Exception {
        String now = DateUtils.formatDate(new Date(), DateUtils.yyyyMMdd);
        String downloadPath = ApplicationProperties.downloadPath + "\\" + now;
        Document document = Jsoup.parse((new URL(url)), 30000);
        Elements imgs = document.getElementsByTag("img");//图片
        Elements videos = document.getElementsByTag("video");//视频
        List<Elements> list = Arrays.asList(imgs, videos);
        String src = "";
        int downloadSize = 0;
        for (Elements elements : list) {
            for (Element el : elements) {
                // System.out.println(el.attr("src"));
                FileUtils.makeFileFolderExists(downloadPath);
                src = el.attr("src");
                if (DownloadByUrlUtils.downloadByUrl(src, UuidUtils.getUuid() + FileUtils.getSuffixName(src), downloadPath)) {
                    downloadSize++;
                }
                //最大下载文件数限制
                if (downloadSize >= limitDownloadMaxSize) {
                    break;
                }
            }
        }
        logger.info("成功下载资源文件==>" + downloadSize);
    }
}
