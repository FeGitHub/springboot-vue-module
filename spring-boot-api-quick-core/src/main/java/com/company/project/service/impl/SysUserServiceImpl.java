package com.company.project.service.impl;

import com.company.project.core.AbstractService;
import com.company.project.core.ServiceException;
import com.company.project.master.dao.SysUserMapper;
import com.company.project.master.model.SysUser;
import com.company.project.service.SysUserService;
import com.company.project.service.TokenCreateService;
import com.company.project.utils.StringUtils;
import com.company.project.utils.UuidUtils;
import com.company.project.utils.ValidationUtil;
import com.company.project.vo.SysUserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2020/08/22.
 */
@Service
@Transactional
public class SysUserServiceImpl extends AbstractService<SysUser> implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;


    @Resource
    private TokenCreateService tokenCreateService;

    /***
     * 注册
     * @param sysUserVo
     * @return
     */
    @Override
    public void saveSysUser(SysUserVo sysUserVo) {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(sysUserVo);
        if (validResult.hasErrors()) {
            String errors = validResult.getErrors();
            throw new ServiceException(errors);
        }
        SysUser sysUser = new SysUser();
        Condition condition = new Condition(SysUser.class);
        condition.createCriteria()
                .andEqualTo("username", sysUser.getUsername());
        List<SysUser> sysUserList = this.findByCondition(condition);
        if (!CollectionUtils.isEmpty(sysUserList)) {
            throw new ServiceException("该用户已存在！");
        }
        BeanUtils.copyProperties(sysUserVo, sysUser); // vo转po
        if (StringUtils.isEmpty(sysUser.getId())) {
            sysUser.setId(UuidUtils.getUuid());
        }
        sysUser.setPassword(DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes()));
        this.save(sysUser);
    }


    /**
     * 登录验证
     *
     * @param sysUserVo
     * @return
     */
    public String checkLogin(SysUserVo sysUserVo) {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(sysUserVo);
        if (validResult.hasErrors()) {
            String errors = validResult.getErrors();
            throw new ServiceException(errors);
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserVo, sysUser); // vo转po
        sysUser.setPassword(DigestUtils.md5DigestAsHex(sysUser.getPassword().getBytes()));
        sysUser = this.mapper.selectOne(sysUser);
        if (sysUser == null || "".equals(sysUser.getId())) {
            throw new ServiceException("账号或密码错误！");
        }
        String TOKENSTR = sysUser.getId() + sysUser.getUsername();
        String TOKEN = DigestUtils.md5DigestAsHex(TOKENSTR.getBytes());
        tokenCreateService.tokenRecord(TOKEN, sysUser.getId(), sysUser.getUsername());
        return TOKEN;
    }
}
