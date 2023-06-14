package com.company.project.utils;

import com.company.project.master.model.SysUser;
import com.company.project.service.CurrentUserService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class Currents {

    public static String TOKEN = "token";

    /**
     * 获取当前用户
     *
     * @return
     */
    public static SysUser getCurrentUser() {
        CurrentUserService currentUserService = (CurrentUserService) SpringBeanUtils.getBean("currentUserServiceImpl");
        return currentUserService.getCurrentUser();
    }

    public static String getHeaderByKey(String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return StringUtils.getStr(request.getHeader(key));
    }


}
