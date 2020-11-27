package com.company.project.service;
import com.company.project.model.ApiLog;
import com.company.project.core.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by CodeGenerator on 2020/11/26.
 */
public interface ApiLogService extends Service<ApiLog> {
    public void saveApiLog(HttpServletRequest request);
}
