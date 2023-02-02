package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.service.TestTableService;
import com.company.project.slave.model.TestTable;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by CodeGenerator on 2023/02/01.
 */
@RestController
@RequestMapping("/test/table")
public class TestTableController {
    @Resource
    private TestTableService testTableService;

    @Autowired
    private com.company.project.service.test.TestFutureService testFutureService;

    @PostMapping("/add")
    public Result add(TestTable testTable) {
        testTableService.save(testTable);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        testTableService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(TestTable testTable) {
        testTableService.update(testTable);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        TestTable testTable = testTableService.findById(id);
        return ResultGenerator.genSuccessResult(testTable);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<TestTable> list = testTableService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/testFuture")
    public Result testFuture() {
        return testFutureService.testFuture();
    }

    @PostMapping("/testFuture2")
    public Result testFuture2() {
        return testFutureService.testFuture2();
    }

}
