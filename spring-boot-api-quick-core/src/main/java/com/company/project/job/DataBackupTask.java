package com.company.project.job;

import com.company.project.utils.MysqlBakDateBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/***
 *  数据备份作业
 */
@Component
public class DataBackupTask {


    private Logger logger = LoggerFactory.getLogger(DataBackupTask.class);

    @Scheduled(cron = "${custom.biz.day.cron}")
    private void process() {
        logger.info("数据备份作业开始");
        try {
            MysqlBakDateBase.backUpData(null, null, null, null, null);
        } catch (Exception e) {
            logger.error("数据备份作业异常：", e);
        }
        logger.info("数据备份作业结束");
    }
}
