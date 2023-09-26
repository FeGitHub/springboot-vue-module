package com.company.project.web.backup;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by CodeGenerator on 2023/02/01.
 */
@RestController
@RequestMapping("/test/table")
public class TestTableController {
   /* @Resource
    private TestTableService testTableService;

    @Autowired
    private TestFutureService testFutureService;


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
*/

}
