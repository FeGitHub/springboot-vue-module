package com.company.project.service.impl;
import com.company.project.core.ServiceException;
import com.company.project.master.dao.PersonMapper;
import com.company.project.master.model.Person;
import com.company.project.service.PersonService;
import com.company.project.core.AbstractService;
import com.company.project.utils.CommUtils;
import com.company.project.utils.ValidationUtil;
import com.company.project.vo.PersonVo;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by jeffqiu
 */
@Service
@Transactional
public class PersonServiceImpl extends AbstractService<Person> implements PersonService {
    @Resource
    private PersonMapper personMapper;

    /***
     * 新增或修改
     * @param personVo
     * @throws ParseException
     */
    @Override
    public void saveOrUpdate(PersonVo personVo) throws Exception {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(personVo);
        if(validResult.hasErrors()){
            String errors = validResult.getErrors();
            throw new ServiceException(errors);
        }
        Person person=new Person();
        BeanUtils.copyProperties(personVo, person); // vo转po
        person.setBirthTime(CommUtils.strToDateYYYMMDD(personVo.getBirthTime()));
        Map<String, Object> param =new HashMap<String,Object>();
        param.put("id",person.getId());
        param.put("identity_number",person.getIdentityNumber());
        Integer existNum=personMapper.is_exist_identity_number(param);
        //保存时，系统需校验身份证号码的唯一性；如果身份证号码存在，不允许保存并提示：该人身份证号码已存在！
        if(existNum>0){
            throw new ServiceException("该人身份证号码已存在！");
        }
        if(StringUtils.isEmpty(person.getId())){
            this.save(person);
        }else{
            Person pBeforeUpdate = this.findById(person.getId());//未更新前的数据内容
            Map<String,Object> personParam = PropertyUtils.describe(person);
            personParam.put("versionNumber",pBeforeUpdate.getVersionNumber());
           // this.update(person);
            Integer isUpdateNum= personMapper.updateOneByMapper(personParam);
            if(isUpdateNum==0){
                throw new ServiceException("当前数据已被其他用户更新，请刷新后操作");
            }
        }
    }





    @Override
    public List<Map<String, Object>> queryMapByMap(Map<String, Object> param) {
        return  personMapper.queryMapByMap(param);
    }

    @Override
    public Integer CNT_Q(Map<String, Object> param) {
        return personMapper.CNT_Q(param);
    }
}
