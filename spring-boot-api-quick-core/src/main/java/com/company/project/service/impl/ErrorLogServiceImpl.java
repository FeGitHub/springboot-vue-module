package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.company.project.core.AbstractService;
import com.company.project.master.dao.ErrorLogMapper;
import com.company.project.master.model.ErrorLog;
import com.company.project.service.ErrorLogService;
import com.company.project.utils.UuidUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * Created by CodeGenerator on 2020/11/10.
 */
@Service
@Transactional
public class ErrorLogServiceImpl extends AbstractService<ErrorLog> implements ErrorLogService {
    @Resource
    private ErrorLogMapper errorLogMapper;

    /***
     * 错误日志信息记录
     * @param request
     * @param e
     */
    @Override
    public String saveErrorLog(HttpServletRequest request, Exception e) {
        String token = request.getHeader("token") == null ? "" : request.getHeader("token");
        ErrorLog log = new ErrorLog();
        log.setToken(token);
        log.setRequestUrl(request.getRequestURI());
        log.setErrorInfo(e.getMessage());
        log.setRequestParams(JSON.toJSONString(request.getParameterMap()));
        log.setId(UuidUtils.getUuid());
        log.setCreateTime(new Date());
        this.save(log);
        return log.getId();
    }
}
