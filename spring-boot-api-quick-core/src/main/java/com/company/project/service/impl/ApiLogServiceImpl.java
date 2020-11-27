package com.company.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.company.project.dao.ApiLogMapper;
import com.company.project.model.ApiLog;
import com.company.project.model.ErrorLog;
import com.company.project.service.ApiLogService;
import com.company.project.core.AbstractService;
import com.company.project.utils.CommUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * Created by CodeGenerator on 2020/11/26.
 */
@Service
@Transactional
public class ApiLogServiceImpl extends AbstractService<ApiLog> implements ApiLogService {
    @Resource
    private ApiLogMapper apiLogMapper;

    @Override
    public void saveApiLog(HttpServletRequest request) {
        String token = request.getHeader("token")==null?"": request.getHeader("token");
        ApiLog log=new ApiLog();
        log.setToken(token);
        log.setRequestIp(CommUtils.getIpAddress(request));
        log.setRequestUrl(request.getRequestURI());
        log.setRequestParams(JSON.toJSONString(request.getParameterMap()));
        log.setId(CommUtils.createUUID());
        log.setCreateTime(new Date());
        this.save(log);
    }
}
