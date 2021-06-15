package com.company.project.service.impl;


import com.company.project.service.TestTableService;
import com.company.project.core.AbstractService;
import com.company.project.slave.dao.TestTableMapper;
import com.company.project.slave.model.TestTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class TestTableServiceImpl extends AbstractService<TestTable> implements TestTableService {
    @Resource
    private TestTableMapper testTableMapper;

    public  TestTable testQuery(){
        TestTable t=new TestTable();
         t.setId("1");
        t=testTableMapper.selectOne(t);
        return t;
    }

}
