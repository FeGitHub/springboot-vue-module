package com.company.project.master.dao;

import com.company.project.core.Mapper;
import com.company.project.master.model.Dicts;

import java.util.List;
import java.util.Map;

public interface DictsMapper extends Mapper<Dicts> {
    List<Map<String, Object>> getDict(String dictstr);
}