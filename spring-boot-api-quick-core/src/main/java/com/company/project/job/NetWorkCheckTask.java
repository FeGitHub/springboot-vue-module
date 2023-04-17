package com.company.project.job;

import com.company.project.constant.SystemLogOperatorType;
import com.company.project.service.impl.SystemLogServiceImpl;
import com.company.project.service.task.NetWorkCheckTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/***
 * 网络维护作业
 */
@Component
public class NetWorkCheckTask {

    @Autowired
    private SystemLogServiceImpl systemLogServiceImpl;


    private Logger logger = LoggerFactory.getLogger(NetWorkCheckTask.class);

    @Autowired
    private NetWorkCheckTaskService netWorkCheckTaskService;


    // @Scheduled(cron = "${custom.biz.network.check.cron}")
    @Scheduled(cron = "${custom.biz.network.test.cron}")
    private void process() {
        String msg = "";
        logger.info("网络检查【开始】...");
        try {
            netWorkCheckTaskService.netWorkCheckTask();
            msg = "网络检查完成";
        } catch (Exception e) {
            logger.error("网络检查【异常】：", e);
            msg = e.getMessage();
        }
        logger.info("网络检查【结束】");
        systemLogServiceImpl.saveLog(SystemLogOperatorType.NET_WORK_CHECK, msg);
    }

}
