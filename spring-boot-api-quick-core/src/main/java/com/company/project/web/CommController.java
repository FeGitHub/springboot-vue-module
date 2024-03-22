package com.company.project.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * 这里的接口是不用token就可以直接访问的
 */
@RestController
@RequestMapping("/comm")
public class CommController {
/*
    public static final Logger log = LoggerFactory.getLogger(CommController.class);
    @Resource
    private DictsService dictsService;


    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private SysUserService sysUserService;


    @PostMapping("/test")
    public void test() {
        log.info("=test=123");
    }

    @Autowired
    private UserConfigServiceImpl userConfigServiceImpl;


    *//***
     * 字典接口
     * @param dicts
     * @return
     *//*
    // @InterceptSql
    @PostMapping("/getDicts")
    public Result getDicts(@RequestParam String dicts) throws Exception {
        userConfigServiceImpl.analysisUserConfig("de51b8f1c528419d9743b618ac4a49f7");
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


    *//**
     * 用户账号注册注册接口
     *
     * @param sysUserVo
     * @return
     *//*
    @PostMapping("/register")
    public Result register(SysUserVo sysUserVo) {
        sysUserService.saveSysUser(sysUserVo);
        return ResultGenerator.genSuccessResult();
    }

    *//**
     * 登录信息验证
     *
     * @param sysUserVo
     * @return
     *//*
    @PostMapping("/login")
    public Result login(SysUserVo sysUserVo) {
        TokenVo tokenVo = sysUserService.checkLogin(sysUserVo);
        redisUtils.set(tokenVo.getToken(), tokenVo.getSysUserId(), 10L, TimeUnit.MINUTES);
        return ResultGenerator.genSuccessResult(tokenVo.getToken());
    }*/

}
