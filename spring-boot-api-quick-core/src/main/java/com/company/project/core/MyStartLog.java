package com.company.project.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(1)
public class MyStartLog implements CommandLineRunner {

    @Value("${server.port}")
    private String port;


    public static final Logger log = LoggerFactory.getLogger(MyStartLog.class);

    @Override
    public void run(String... args) {
        log.info("+ =================================================================================");
        log.info("+                        项目启动成功!!!                                           ");
        log.info("+        swagger访问地址:http://localhost:" + port + "/swagger-ui.html             ");
        log.info("+ =================================================================================");
    }
}

