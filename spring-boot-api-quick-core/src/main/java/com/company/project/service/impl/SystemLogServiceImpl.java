package com.company.project.service.impl;

import com.company.project.core.AbstractService;
import com.company.project.master.dao.SystemLogMapper;
import com.company.project.master.model.SystemLog;
import com.company.project.service.SystemLogService;
import com.company.project.utils.UuidUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2023/04/17.
 */
@Service
@Transactional
public class SystemLogServiceImpl extends AbstractService<SystemLog> implements SystemLogService {
    @Resource
    private SystemLogMapper systemLogMapper;


    @Transactional
    public void saveLog(String type, String msg) {
        SystemLog systemLog = new SystemLog();
        systemLog.setId(UuidUtils.getUuid());
        systemLog.setType(type);
        systemLog.setMessage(msg);
        save(systemLog);
    }

}
