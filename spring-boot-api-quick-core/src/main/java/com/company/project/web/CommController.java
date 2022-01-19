package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.dubbo.service.DubboTestService;
import com.company.project.kafka.producer.UserLogProducer;
import com.company.project.service.DictsService;
import com.company.project.service.SysUserService;
import com.company.project.service.TestTableService;
import com.company.project.slave.model.TestTable;
import com.company.project.utils.RedisUtils;
import com.company.project.vo.SysUserVo;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/***
 * 这里的接口是不用token就可以直接访问的
 */
@RestController
@RequestMapping("/comm")
public class CommController {

    public static final Logger log = LoggerFactory.getLogger(CommController.class);
    @Resource
    private DictsService dictsService;

    @Autowired
    TestTableService testTableService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SysUserService sysUserService;

    @Reference(version = "1.0.0")
    private DubboTestService dubboTestService;


    @Autowired
    private UserLogProducer kafkaSender;

    /***
     * 字典接口
     * @param dicts
     * @return
     */
    @PostMapping("/getDicts")
    public Result getDicts(@RequestParam String dicts ) {
        String[] arr = dicts.split(",");
        Map<String,Object> rtnMap = new HashMap<String, Object>();
        for(String dictStr : arr){
            List<Map<String, Object>> list= dictsService.getDict(dictStr);
            if(list!=null&&list.size()>0){
                rtnMap.put(dictStr, list);
            }
        }
        return ResultGenerator.genSuccessResult(rtnMap);
    }

    @PostMapping("/testSlaveQuery")
    public Result testSlaveQuery() {
        TestTable t= testTableService.testQuery();
        return ResultGenerator.genSuccessResult(t);
    }



    /**
     * 用户账号注册注册接口
     * @param sysUserVo
     * @return
     */
    @PostMapping("/register")
    public Result register(SysUserVo sysUserVo) {
        sysUserService.saveSysUser(sysUserVo);
        return ResultGenerator.genSuccessResult();
    }
    /**
     * 登录信息验证
     * @param sysUserVo
     * @return
     */
    @PostMapping("/login")
    public Result login(SysUserVo sysUserVo) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String lastUpdateStr= df.format(new Date()).toString();//最后更新时间
        String tokenMd5=sysUserService.checkLogin(sysUserVo);
        if("".equals(tokenMd5)){
            throw new ServiceException("登录验证失败！");
        }
        redisUtils.set(tokenMd5,lastUpdateStr,10L,TimeUnit.MINUTES);
        return ResultGenerator.genSuccessResult(tokenMd5);
    }



    /***
     * 删除或新增用于测试的token数据信息
     * @param type
     * @return
     */
    @PostMapping("/testAddOrDelRedis")
    public Result testAddOrDelRedis(@NotNull(message = "操作类型不能为空") @RequestParam String type) {
        String token="a38b4b83d97cac745529ea3dbb587b68";//用于测试的token信息
        boolean hasKey = redisUtils.exists(token);
        if("ADD".equals(type)){
            if(!hasKey){
                redisUtils.set(token,"TEST",10L,TimeUnit.MINUTES);
                log.info("数据插入缓存..." );
            }
        }else if("DEL".equals(type)){
            if(hasKey){
                redisUtils.remove(token);
                log.info("数据删除缓存..." );
            }
        }else{
            throw new ServiceException("只能输入ADD,DEL操作类型！");
        }
        return ResultGenerator.genSuccessResult();
    }


    /***
     * 测试dubbo
     * @return
     */
   @PostMapping("/testDubbo")
    public Result testDubbo() {
        String retMsg=dubboTestService.testDubboService();
        log.info("dubboService:"+retMsg);
       return ResultGenerator.genSuccessResult(retMsg);
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

  /*  @PostMapping("/testRedis")
    public Result testRedis(@NotNull(message = "主键信息不能为空") @RequestParam String id) {
        boolean hasKey = redisUtils.exists(id);
        if(hasKey){
            //获取缓存
            Object object =  redisUtils.get(id);
            log.info("从缓存获取的数据:"+ object);
        }else{
            //从数据库中获取信息
            log.info("从数据库中获取数据");
            // str = testService.test();
            String str="redis存储信息";
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisUtils.set(id,str,10L,TimeUnit.MINUTES);
            log.info("数据插入缓存:" + str);
        }
        return ResultGenerator.genSuccessResult();
    }  */

}
