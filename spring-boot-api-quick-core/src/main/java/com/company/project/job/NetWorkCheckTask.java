package com.company.project.job;

import com.company.project.service.task.NetWorkCheckTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NetWorkCheckTask {

    private Logger logger = LoggerFactory.getLogger(NetWorkCheckTask.class);

    @Autowired
    private NetWorkCheckTaskService netWorkCheckTaskService;


    // @Scheduled(cron = "${custom.biz.network.check.cron}")
    @Scheduled(cron = "${custom.biz.network.test.cron}")
    private void process() {
        logger.info("网络检查【开始】...");
        try {
            netWorkCheckTaskService.netWorkCheckTask();
        } catch (Exception e) {
            logger.error("网络检查【异常】：", e);
        }
        logger.info("网络检查【结束】");
    }

}
