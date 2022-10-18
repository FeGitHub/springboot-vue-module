package com.company.project.web;

import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.service.DictsService;
import com.company.project.service.SysUserService;
import com.company.project.utils.DateUtils;
import com.company.project.utils.RedisUtils;
import com.company.project.utils.StringUtils;
import com.company.project.vo.SysUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private RedisUtils redisUtils;

    @Autowired
    private SysUserService sysUserService;


    /***
     * 字典接口
     * @param dicts
     * @return
     */
    @PostMapping("/getDicts")
    public Result getDicts(@RequestParam String dicts) {
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


    /**
     * 用户账号注册注册接口
     *
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
     *
     * @param sysUserVo
     * @return
     */
    @PostMapping("/login")
    public Result login(SysUserVo sysUserVo) {
        String lastUpdateStr = DateUtils.formatDate(new Date(), DateUtils.yyyy_MM_dd_HH_mm_ss);//最后更新时间
        String tokenMd5 = sysUserService.checkLogin(sysUserVo);
        if (StringUtils.isEmpty(tokenMd5)) {
            throw new ServiceException("登录验证失败！");
        }
        redisUtils.set(tokenMd5, lastUpdateStr, 10L, TimeUnit.MINUTES);
        return ResultGenerator.genSuccessResult(tokenMd5);
    }

}
