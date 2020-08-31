package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.service.SysUserService;
import com.company.project.utils.RedisUtils;
import com.company.project.vo.SysUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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


    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SysUserService sysUserService;


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
        return ResultGenerator.genSuccessResult();
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


}
