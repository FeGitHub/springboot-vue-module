package com.company.project.service.impl;

import com.company.project.master.dao.DictsMapper;
import com.company.project.master.model.Dicts;
import com.company.project.service.DictsService;
import com.company.project.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by CodeGenerator on 2020/08/19.
 */
@Service
@Transactional
public class DictsServiceImpl extends AbstractService<Dicts> implements DictsService {
    @Resource
    private DictsMapper dictsMapper;

    @Override
    public List<Map<String, Object>> getDict(String ITEMCODE) {
        return dictsMapper.getDict(ITEMCODE);
    }
}
