package com.company.project.job;

import com.company.project.constant.SystemLogOperatorType;
import com.company.project.service.impl.SystemLogServiceImpl;
import com.company.project.service.task.DataOperatorTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/***
 *  每天处理作业
 */
@Component
public class DayOperatorTask {

    @Autowired
    private SystemLogServiceImpl systemLogServiceImpl;

    @Autowired
    private DataOperatorTaskService dataOperatorTaskService;

    private Logger logger = LoggerFactory.getLogger(DayOperatorTask.class);

    @Scheduled(cron = "${custom.biz.day.clockAt1Am.cron}")
    // @Scheduled(cron = "${custom.biz.network.test.cron}")
    private void process() {
        String msg = "";
        logger.info("每日作业开始");
        try {
            dataOperatorTaskService.backUpData();
            dataOperatorTaskService.delData();
            msg = "每日作业开始";
        } catch (Exception e) {
            logger.error("每日作业开始异常：", e);
            msg = e.getMessage();
        }
        logger.info("每日作业开始结束");
        systemLogServiceImpl.saveLog(SystemLogOperatorType.DAY_OPERATOR, msg);
    }
}
