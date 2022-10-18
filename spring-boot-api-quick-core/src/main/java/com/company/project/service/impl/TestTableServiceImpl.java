package com.company.project.service.impl;


import com.company.project.core.AbstractService;
import com.company.project.service.TestTableService;
import com.company.project.slave.dao.TestTableMapper;
import com.company.project.slave.model.TestTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2022/10/18.
 */
@Service
@Transactional
public class TestTableServiceImpl extends AbstractService<TestTable> implements TestTableService {
    @Resource
    private TestTableMapper testTableMapper;

}
