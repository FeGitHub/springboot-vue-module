package com.company.project.service;
import com.company.project.master.model.TokenCreate;
import com.company.project.core.Service;


/**
 * Created by CodeGenerator on 2020/11/26.
 */
public interface TokenCreateService extends Service<TokenCreate> {
    void tokenRecord(String token,String userId,String username);
}
