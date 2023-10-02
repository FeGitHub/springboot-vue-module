package com.company.project.service.base;

import com.company.project.constant.ApplicationProperties;
import com.company.project.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InitService {

    @Autowired
    private MailService mailService;

    @Transactional
    public void init() {
        ApplicationProperties.templatePath = this.getClass().getResource("/").getPath() + "template";
        mailService.initMailUtils();
    }
}
