package com.company.project.scheduler.job;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.company.project.service.TestTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
public class TestJob {
    private static Logger logger = LoggerFactory.getLogger(TestJob.class);
    @Autowired
    public TestTableService testTableService;

    @PostConstruct //启动项目先执行
    private void process() {
        logger.info("===========================================");

    }
}
