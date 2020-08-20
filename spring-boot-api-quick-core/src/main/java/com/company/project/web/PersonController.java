package com.company.project.web;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.core.ServiceException;
import com.company.project.model.Person;
import com.company.project.service.PersonService;
import com.company.project.utils.CommUtils;
import com.company.project.utils.ValidationUtil;
import com.company.project.vo.PersonVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.beanutils.PropertyUtils;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by CodeGenerator on 2020/08/18.
*/
@RestController
@RequestMapping("/person")
public class PersonController {
    @Resource
    private PersonService personService;

    @PostMapping("/add")
    public Result add(PersonVo personVo) throws Exception {
        personService.save(personVo);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@NotNull(message = "主键信息不能为空") @RequestParam String id) {
        personService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Person person) {
        if(StringUtils.isEmpty(person.getId())){
            throw new ServiceException("主键信息不能为空！");
        }
        personService.update(person);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@NotNull(message = "主键信息不能为空") @RequestParam String id) throws Exception {
        Person person = personService.findById(id);
        Map<String,Object> rtMap = PropertyUtils.describe(person);
        rtMap.put("birthTime", CommUtils.timestampToDateYYYMMDD(person.getBirthTime()));
        return ResultGenerator.genSuccessResult(rtMap);
    }


    @PostMapping("/queryList")
    public Result queryList(PersonVo personVo) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String,Object> param = PropertyUtils.describe(personVo);
        Map<String,Object> rtMap = new HashMap<String,Object>();
        List<Map<String ,Object>> resultMap=personService.queryMapByMap(param);
        Integer CNT=personService.CNT_Q(param);
        rtMap.put("dataset",resultMap);
        rtMap.put("total",CNT);
        return ResultGenerator.genSuccessResult(rtMap);
    }
}
