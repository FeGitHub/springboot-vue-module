package com.company.project.service.test;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.service.TestTableService;
import com.company.project.slave.model.TestTable;
import com.company.project.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.concurrent.Future;

@Service
public class TestFutureService {

    @Autowired
    private TestTableService testTableService;

    @Async
    public Future<String> asyncBuildProcA() throws InterruptedException {
        Thread.sleep(1000);
        return new AsyncResult<String>("success");
    }

    @Async
    public Future<TestTable> asyncBuildProcB() {
        TestTable testTable = new TestTable();
        testTable.setId(UuidUtils.getUuid());
        testTable.setTestName("1");
        testTableService.save(testTable);
        return new AsyncResult<>(testTable);
    }

    public void saveTestTable(TestTable testTable) {
        testTable.setTestRemark("2");
        testTableService.update(testTable);
    }

    @Transactional(value = "slaveTransactionManager")
    public Result testFuture() {
        Future<String> futureA = null;
        Future<TestTable> futureB = null;
        try {
            long startProcTime = System.currentTimeMillis();
            futureA = asyncBuildProcA();
            futureB = asyncBuildProcB();
            while (true) {
                if (futureA.isDone() && futureB.isDone()) {
                    TestTable testTable = futureB.get();
                    saveTestTable(testTable);
                    break;
                } else if (System.currentTimeMillis() - startProcTime > 59000) {
                    //超時提醒
                    return ResultGenerator.genFailResult("处理超时，请重试");
                }
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
            throw new RuntimeException("操作失败");
        }
        return ResultGenerator.genSuccessResult();
    }


    @Transactional(value = "slaveTransactionManager")
    public Result testFuture2() {
        TestTable testTable = new TestTable();
        testTable.setId(UuidUtils.getUuid());
        testTable.setTestName("1");
        testTableService.save(testTable);
        if (true) {
            // throw new RuntimeException("操作失败");
        }
        return ResultGenerator.genSuccessResult();
    }
}
