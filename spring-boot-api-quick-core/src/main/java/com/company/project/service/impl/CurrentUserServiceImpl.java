package com.company.project.service.impl;

import com.company.project.constant.BasicServiceMessage;
import com.company.project.core.ServiceException;
import com.company.project.master.model.SysUser;
import com.company.project.service.CurrentUserService;
import com.company.project.service.SysUserService;
import com.company.project.utils.Currents;
import com.company.project.utils.RedisUtils;
import com.company.project.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Autowired
    private RedisUtils redisUtils;


    @Autowired
    private SysUserService sysUserService;

    @Override
    public SysUser getCurrentUser() {
        //获取当前登录用户的token  可以通过这个
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = StringUtils.getStr(request.getHeader(Currents.TOKEN));//头部参数携带token
        String sysUserId = StringUtils.getStr(redisUtils.get(token));
        SysUser sysUser = sysUserService.findById(sysUserId);
        if (sysUser == null) {
            throw new ServiceException(BasicServiceMessage.INVALID_LOG_INFO);
        }
        return sysUser;
    }
}
