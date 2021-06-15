package com.company.project.master.dao;

import com.company.project.core.Mapper;
import com.company.project.master.model.Person;

import java.util.List;
import java.util.Map;

public interface PersonMapper extends Mapper<Person> {
    /**
     * 具体页面数据
     * @param param
     * @return
     */
    List<Map<String, Object>> queryMapByMap(Map<String, Object> param);

    /**
     *  总记录数
     * @param param
     * @return
     */
    Integer CNT_Q(Map<String, Object> param);


    Integer is_exist_identity_number(Map<String, Object> param);

    Integer updateOneByMapper(Map<String, Object> param);
}