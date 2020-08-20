package com.company.project.web;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.company.project.core.Result;
import com.company.project.core.ResultGenerator;
import com.company.project.model.Dicts;
import com.company.project.service.DictsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/comm")
public class CommController {
    @Resource
    private DictsService dictsService;

    @PostMapping("/getDicts")
    public Result getDicts(@RequestParam String dicts ) {
        String[] arr = dicts.split(",");
        Map<String,Object> rtnMap = new HashMap<String, Object>();
        for(String dictStr : arr){
            List<Map<String, Object>> list= dictsService.getDict(dictStr);
            if(list!=null&&list.size()>0){
                rtnMap.put(dictStr, list);
            }
        }
        return ResultGenerator.genSuccessResult(rtnMap);
    }


}
