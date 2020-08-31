package com.company.project.service;
import com.company.project.model.SysUser;
import com.company.project.core.Service;
import com.company.project.vo.SysUserVo;

import java.util.Map;


/**
 * Created by CodeGenerator on 2020/08/22.
 */
public interface SysUserService extends Service<SysUser> {
   void saveSysUser(SysUserVo sysUserVo);

   String  checkLogin(SysUserVo sysUserVo);
}
