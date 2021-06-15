package com.company.project.service;
import com.company.project.master.model.Dicts;
import com.company.project.core.Service;
import java.util.List;
import java.util.Map;
/**
 * Created by jeffqiu
 */
public interface DictsService extends Service<Dicts> {
    /***
     *
     * @param dictstr  "字典A,字典B..."
     * @return
     */
    List<Map<String, Object>> getDict(String dictstr);
}
