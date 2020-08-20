package com.company.project.dao;

import com.company.project.core.Mapper;
import com.company.project.model.Person;

import java.util.List;
import java.util.Map;

public interface PersonMapper extends Mapper<Person> {
    List<Map<String, Object>> queryMapByMap(Map<String, Object> param);

    Integer CNT_Q(Map<String, Object> param);
}