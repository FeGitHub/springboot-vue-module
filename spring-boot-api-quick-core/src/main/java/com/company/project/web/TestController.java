package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.dubbo.service.DubboTestService;
import com.company.project.kafka.producer.UserLogProducer;
import com.company.project.service.DictsService;
import com.company.project.service.TestTableService;
import com.company.project.service.download.DownloadService;
import com.company.project.slave.model.TestTable;
import com.company.project.utils.ExportExcelUtil;
import com.company.project.utils.HttpURLConnectionUtils;
import com.company.project.utils.RedisUtils;
import com.company.project.vo.TestValidationVo;
import nl.flotsam.xeger.Xeger;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;


/****
 * 测试控制器
 */
@RestController
@RequestMapping("/test")
public class TestController {

    public static final Logger log = LoggerFactory.getLogger(TestController.class);
    @Resource
    private DictsService dictsService;


    @Autowired
    private RedisUtils redisUtils;


    @Reference(version = "1.0.0")
    private DubboTestService dubboTestService;


    @Autowired
    private UserLogProducer kafkaSender;

    @Autowired
    private TestTableService testTableService;

    @Autowired
    private DownloadService downloadService;


    /***
     *  原生poi生成excel文件
     * @param response
     */
    @GetMapping("/downloadCustomExcel")
    // http://localhost:8085/test/downloadCustomExcel
    public void downloadToFrequency(HttpServletResponse response) {
        ExportExcelUtil.CreateExcelDataVo createExcelDataVo = new ExportExcelUtil.CreateExcelDataVo();
        createExcelDataVo.setTitle("标题");
        createExcelDataVo.setSheetName("excel");
        createExcelDataVo.setMergeColumn(new int[]{0});//表示第一列有临近相同的合并成同一个
        createExcelDataVo.setHeaders(new ArrayList<String>(Arrays.asList("甲", "乙", "丙")));
        String[][] content = {
                {"1", "2", "3"},
                {"1", "5", "6"},
                {"B", "8", "9"},
                {"B", "8", "9"},
                {"A", "2", "3"},
                {"A", "5", "6"},
                {"A", "8", "9"}
        };
        createExcelDataVo.setContent(content);
        HSSFWorkbook hSSFWorkbook = ExportExcelUtil.createHSSFWorkbook(createExcelDataVo);
        //这里可以自定义处理
        ExportExcelUtil.createExcel(hSSFWorkbook, response, "下载");
    }


    /***
     * 字典接口
     * @param dicts
     * @return
     */
    @PostMapping("/testDicts")
    public Result testDicts(@RequestParam String dicts) {
        String[] arr = dicts.split(",");
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        for (String dictStr : arr) {
            List<Map<String, Object>> list = dictsService.getDict(dictStr);
            if (list != null && list.size() > 0) {
                rtnMap.put(dictStr, list);
            }
        }
        return ResultGenerator.genSuccessResult(rtnMap);
    }


    /***
     * 删除或新增用于测试的token数据信息
     * @param type
     * @return
     */
    @PostMapping("/testAddOrDelRedis")
    public Result testAddOrDelRedis(@NotNull(message = "操作类型不能为空") @RequestParam String type) {
        String token = "a38b4b83d97cac745529ea3dbb587b68";//用于测试的token信息
        boolean hasKey = redisUtils.exists(token);
        String tip;
        if ("ADD".equals(type)) {
            if (!hasKey) {
                redisUtils.set(token, "TEST", 10L, TimeUnit.MINUTES);
                tip = "数据成功插入缓存";
            } else {
                tip = "该数据缓存已存在，不做重复插入";
            }
        } else if ("DEL".equals(type)) {
            if (hasKey) {
                redisUtils.remove(token);
                tip = "数据成功在缓存去除";
            } else {
                tip = "该数据在缓存不存在，无需删除";
            }
        } else {
            throw new ServiceException("只能输入ADD,DEL操作类型！");
        }
        return ResultGenerator.genSuccessResult(tip);
    }


    /***
     * 测试dubbo
     * @return
     */
    @PostMapping("/testDubbo")
    public Result testDubbo() {
        String retMsg = dubboTestService.testDubboService();
        log.info("dubboService:" + retMsg);
        return ResultGenerator.genSuccessResult(retMsg);
    }


    /****
     * 测试第二数据库
     * @return
     */
    @PostMapping("/testSlaveQuery")
    public Result testSlaveQuery() {
        List<TestTable> list = testTableService.findAll();
        return ResultGenerator.genSuccessResult(list);
    }


    /***
     * 测试 檢驗
     * @return
     */
    @PostMapping("/testValidation")
    public Result testValidation(TestValidationVo testValidationVo) {
     /*   ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(testValidationVo);
        if (validResult.hasErrors()) {
            String errors = validResult.getErrors();
            throw new ServiceException(errors);
        } */
        return ResultGenerator.genSuccessResult("请求vo检验通过");
    }


    /***
     * 测试kafka
     * @return
     */
    @PostMapping("/testKafka")
    public Result testKafka() {
        for (int i = 0; i < 10; i++) {
            //调用消息发送类中的消息发送方法
            kafkaSender.sendLog(String.valueOf(i));
        }
        return ResultGenerator.genSuccessResult();
    }


    /****
     * 测试请求url
     * @return
     */
    @PostMapping("/testPostUrl")
    public Result testPostUrl() {
        String url = "http://192.168.186.129:8085/comm/getDicts?dicts=GENDER";
        String result = HttpURLConnectionUtils.doPostByJson(url, null);
        return ResultGenerator.genSuccessResult(result);
    }


    /****
     * 根据正则生成随机符合的字符串
     * @return
     */
    @PostMapping("/createRegexStr")
    public Result createRegexStr() {
        String regex = "[0-9]{1,2}";
        Xeger generator = new Xeger(regex);
        for (int i = 0; i < 10; i++) {
            String result = generator.generate();
            log.info(result);
        }
        return ResultGenerator.genSuccessResult();
    }


    /****
     * 下载资源测试
     * @return
     */
    @PostMapping("/getPictureAndVideoByUrl")
    public Result getPictureAndVideoByUrl() throws Exception {
        String url = "https://www.baidu.com/";
        downloadService.getPictureAndVideoByUrl(url);
        return ResultGenerator.genSuccessResult();
    }


}
