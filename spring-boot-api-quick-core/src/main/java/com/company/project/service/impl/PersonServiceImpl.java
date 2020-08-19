package com.company.project.service.impl;

import com.company.project.core.ServiceException;
import com.company.project.dao.PersonMapper;
import com.company.project.model.Person;
import com.company.project.service.PersonService;
import com.company.project.core.AbstractService;
import com.company.project.utils.ValidationUtil;
import com.company.project.vo.PersonVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2020/08/18.
 */
@Service
@Transactional
public class PersonServiceImpl extends AbstractService<Person> implements PersonService {
    @Resource
    private PersonMapper personMapper;

    @Override
    public void save(PersonVo personVo) {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(personVo);
        if(validResult.hasErrors()){
            String errors = validResult.getErrors();
            throw new ServiceException(errors);
        }
        Person person=new Person();
        BeanUtils.copyProperties(personVo, person); // vo转po
            this.save(person);

    }

    @Override
    public List<Map<String, Object>> queryMapByMap(Map<String, Object> param) {
        return  personMapper.queryMapByMap(param);
    }
}
