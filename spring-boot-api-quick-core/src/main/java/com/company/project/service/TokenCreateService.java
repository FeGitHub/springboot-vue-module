package com.company.project.service;
import com.company.project.model.TokenCreate;
import com.company.project.core.Service;
import com.company.project.vo.SysUserVo;


/**
 * Created by CodeGenerator on 2020/11/26.
 */
public interface TokenCreateService extends Service<TokenCreate> {
    void tokenRecord(String token,String userId,String username);
}
