package com.company.project.dubbo.service.impl;
import com.company.project.dubbo.service.DubboTestService;
import org.apache.dubbo.config.annotation.Service;

@Service(version = "1.0.0",interfaceClass = DubboTestService.class)
public class DubboTestServiceImpl implements DubboTestService {

    @Override
    public String testDubboService() {
        return "dubbo提供者输出";
    }
}
