package com.company.project.job;

import com.company.project.constant.SystemLogOperatorType;
import com.company.project.service.impl.SystemLogServiceImpl;
import com.company.project.utils.MysqlBakDateBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/***
 *  数据备份作业
 */
@Component
public class DataBackupTask {

    @Autowired
    private SystemLogServiceImpl systemLogServiceImpl;


    private Logger logger = LoggerFactory.getLogger(DataBackupTask.class);

    @Scheduled(cron = "${custom.biz.day.clockAt2Am.cron}")
    private void process() {
        String msg = "";
        logger.info("数据备份作业开始");
        try {
            MysqlBakDateBase.backUpData(null, null, null, null, null);
            msg = "数据备份结束";
        } catch (Exception e) {
            logger.error("数据备份作业异常：", e);
            msg = e.getMessage();
        }
        logger.info("数据备份作业结束");
        systemLogServiceImpl.saveLog(SystemLogOperatorType.DATA_BACK_UP, msg);
    }
}
