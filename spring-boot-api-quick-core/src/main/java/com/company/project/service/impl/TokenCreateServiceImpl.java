package com.company.project.service.impl;

import com.company.project.dao.TokenCreateMapper;
import com.company.project.model.TokenCreate;
import com.company.project.service.TokenCreateService;
import com.company.project.core.AbstractService;
import com.company.project.utils.CommUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;


/**
 * Created by CodeGenerator on 2020/11/26.
 */
@Service
@Transactional
public class TokenCreateServiceImpl extends AbstractService<TokenCreate> implements TokenCreateService {
    @Resource
    private TokenCreateMapper tokenCreateMapper;

    @Override
    public void tokenRecord(String token, String userId, String userName) {
        TokenCreate tokenCreate=new TokenCreate();
        tokenCreate.setId(CommUtils.createUUID());
        tokenCreate.setToken(token);
        tokenCreate.setCreateTime(new Date());
        tokenCreate.setUserId(userId);
        tokenCreate.setUserName(userName);
        this.save(tokenCreate);
    }
}
