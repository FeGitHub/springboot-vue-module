package com.company.project.service;
import com.company.project.model.Dicts;
import com.company.project.core.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2020/08/19.
 */
public interface DictsService extends Service<Dicts> {
    List<Map<String, Object>> getDict(String dictstr);
}
