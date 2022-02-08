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

}
