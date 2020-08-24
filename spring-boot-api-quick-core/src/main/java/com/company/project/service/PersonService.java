package com.company.project.service;
import com.company.project.model.Person;
import com.company.project.core.Service;
import com.company.project.vo.PersonVo;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2020/08/18.
 */
public interface PersonService extends Service<Person> {
    void saveOrUpdate(PersonVo personVo) throws ParseException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, Exception;

    List<Map<String ,Object>> queryMapByMap(Map<String ,Object> param);

    Integer CNT_Q(Map<String, Object> param);
}
