package com.company.project.service;

import com.company.project.core.Service;
import com.company.project.slave.model.TestTable;

/**
 * Created by CodeGenerator on 2021/06/14.
 */
public interface TestTableService extends Service<TestTable> {
    public  TestTable testQuery();
}
