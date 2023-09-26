package com.company.project.service.impl;

import com.company.project.master.dao.ConfigMapper;
import com.company.project.master.model.Config;
import com.company.project.service.ConfigService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
* Created by CodeGenerator on 2023/09/26.
*/
@Service
@Transactional
public class ConfigServiceImpl extends AbstractService<Config> implements ConfigService {
@Resource
private ConfigMapper configMapper;

}
