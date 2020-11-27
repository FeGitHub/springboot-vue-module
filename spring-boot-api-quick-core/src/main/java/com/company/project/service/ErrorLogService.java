package com.company.project.service;
import com.company.project.model.ErrorLog;
import com.company.project.core.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by CodeGenerator on 2020/11/10.
 */
public interface ErrorLogService extends Service<ErrorLog> {

    public void saveErrorLog(HttpServletRequest request,Exception e);

}
