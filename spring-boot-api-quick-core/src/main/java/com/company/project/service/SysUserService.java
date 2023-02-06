package com.company.project.service;

import com.company.project.core.Service;
import com.company.project.master.model.SysUser;
import com.company.project.vo.SysUserVo;
import com.company.project.vo.TokenVo;


/**
 * Created by CodeGenerator on 2020/08/22.
 */
public interface SysUserService extends Service<SysUser> {
    void saveSysUser(SysUserVo sysUserVo);

    TokenVo checkLogin(SysUserVo sysUserVo);
}
