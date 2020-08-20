package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.model.Person;
import com.company.project.service.PersonService;
import com.company.project.utils.CommUtils;
import com.company.project.vo.PersonVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.beanutils.PropertyUtils;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by jeffqiu
 */
@RestController
@RequestMapping("/person")
public class PersonController {
    @Resource
    private PersonService personService;

    /***
     * 新增
     * @param personVo
     * @return
     * @throws Exception
     */
    @PostMapping("/add")
    public Result add(PersonVo personVo) throws Exception {
        personService.saveOrUpdate(personVo);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 物理删除
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public Result delete(@NotNull(message = "主键信息不能为空") @RequestParam String id) {
       personService.deleteById(id);
       return ResultGenerator.genSuccessResult();
    }

    /**
     * 更新
     * @param personVo
     * @return
     * @throws Exception
     */
    @PostMapping("/update")
    public Result update(PersonVo personVo) throws Exception {
        if(StringUtils.isEmpty(personVo.getId())){
            throw new ServiceException("主键信息不能为空！");
        }
        personService.saveOrUpdate(personVo);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 详情信息
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/detail")
    public Result detail(@NotNull(message = "主键信息不能为空") @RequestParam String id) throws Exception {
        Person person = personService.findById(id);
        Map<String,Object> rtMap = PropertyUtils.describe(person);
        rtMap.put("birthTime", CommUtils.timestampToDateYYYMMDD(person.getBirthTime()));
        return ResultGenerator.genSuccessResult(rtMap);
    }


    /***
     * 查询分页信息
     * @param personVo
     * @return
     * @throws Exception
     */
    @PostMapping("/queryList")
    public Result queryList(PersonVo personVo) throws Exception {
        Map<String,Object> param = PropertyUtils.describe(personVo);
        Map<String,Object> rtMap = new HashMap<String,Object>();
        List<Map<String ,Object>> resultMap=personService.queryMapByMap(param);
        Integer CNT=personService.CNT_Q(param);
        rtMap.put("dataset",resultMap);//具体页面数据
        rtMap.put("total",CNT);//总记录数
        return ResultGenerator.genSuccessResult(rtMap);
    }
}
