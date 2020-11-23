package com.company.project.service.impl;
import com.company.project.dao.ErrorLogMapper;
import com.company.project.model.ErrorLog;
import com.company.project.service.ErrorLogService;
import com.company.project.core.AbstractService;
import com.company.project.utils.CommUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;


/**
 * Created by CodeGenerator on 2020/11/10.
 */
@Service
@Transactional
public class ErrorLogServiceImpl extends AbstractService<ErrorLog> implements ErrorLogService {
    @Resource
    private ErrorLogMapper errorLogMapper;

    @Override
    public void saveByUuid(ErrorLog log) {
        log.setId(CommUtils.createUUID());
        log.setCreateTime(new Date());
        this.save(log);
    }
}
