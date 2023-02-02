package com.company.project.service.test;

import com.company.project.slave.model.TestTable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class AsyncService {
    @Async
    public Future<String> asyncBuildProcA() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("asyncBuildProcA：" + Thread.currentThread().getName());
        return new AsyncResult<String>("success");
    }

    @Async
    public Future<TestTable> asyncBuildProcB() throws InterruptedException {
        TestTable testTable = new TestTable();
        Thread.sleep(3000);
        System.out.println("asyncBuildProcB:" + Thread.currentThread().getName());
        return new AsyncResult<>(testTable);
    }

}
