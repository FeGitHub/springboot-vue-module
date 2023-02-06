package com.company.project.utils;

import com.company.project.master.model.SysUser;
import com.company.project.service.CurrentUserService;

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
}
