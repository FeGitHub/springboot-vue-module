package com.test;

import com.company.project.utils.CmdUtil;
import com.company.project.utils.MapToBeanUtils;
import com.company.project.utils.StringConvertUtil;
import com.test.demo.mapStruct.PersonTest;
import com.test.demo.mapStruct.PersonTestDto;
import com.test.demo.mapStruct.PersonTestMapper;
import nl.flotsam.xeger.Xeger;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUnit {
    public static void main(String[] args) {
        String regex = "[0-9]{1,2}";
        Xeger generator = new Xeger(regex);
        for (int i = 0; i < 10; i++) {
            String result = generator.generate();
            System.out.println(result);
        }
    }

    @Test
    public void test() {
        PersonTest person = new PersonTest();
        person.setDescribe("测试");
        person.setAge(18);
        person.setName("张三");
        person.setHeight(170.5);
        person.setSource(new BigDecimal("100"));
        // person.setCreateTime(new Date());
        PersonTestDto dto = PersonTestMapper.INSTANCT.conver(person);

        System.out.println(dto);
        // PersonDTO(describe=测试, id=null, personName=张三, age=18, source=100, height=170.5)
    }


    @Test
    public void testMapToBean() {
        PersonTest person = new PersonTest();
        MapToBeanUtils.MapToBeanConfig config = new MapToBeanUtils.MapToBeanConfig();
        Map<String, Object> map = new HashMap<>();
        map.put("describe", "describe");
        map.put("id", "id");
        map.put("name", "name");
        map.put("age", "16");
        map.put("source", "164");
        map.put("testBoolean1", "true");
        map.put("testBoolean2", "true");
        map.put("height", "3");
        map.put("createTime", "2020-01-01 23:23:24");
        MapToBeanUtils.mapToBean(map, person, config);
    }


    @Test
    public void testStringConvertUtil() {
        System.out.println(StringConvertUtil.convertTimeFormat("2020-01-01"));
        System.out.println(StringConvertUtil.convertTimeFormat("2020-01"));
        System.out.println(StringConvertUtil.convertTimeFormat("2020-01-01 23:23:23"));
        System.out.println(StringConvertUtil.convertTimeFormat("2020年01月01日"));
    }

    @Test
    public void intersectOfLocalDate() {
        boolean result;
        LocalDate startDateByA = StringConvertUtil.convertLocalDate("2023-04-10", null);
        LocalDate endDateByA = StringConvertUtil.convertLocalDate("2023-04-20", null);
        LocalDate startDateByB = StringConvertUtil.convertLocalDate("2023-04-02", null);
        LocalDate endDateByB = StringConvertUtil.convertLocalDate("2023-04-12", null);

        LocalDate maxDate = LocalDate.now().plusYears(10000);
        endDateByA = endDateByA == null ? maxDate : endDateByA;
        endDateByB = endDateByB == null ? maxDate : endDateByB;
        //存在于右侧或左侧
        //校验前提：开始日期一定是小于或等于结束日期
        //（B端的开始日期大于或者等于A端的结束日期）||（B端的结束日期小于或等于A端的开始日期）
        result = (startDateByB.isAfter(endDateByA) || startDateByB.isEqual(endDateByA))
                || (
                endDateByB.isBefore(startDateByA) || endDateByB.isEqual(startDateByA)
        );
        if (result) {
            System.out.println("不存在交集");
        } else {
            System.out.println("存在交集");
        }
    }

    @Test
    public void testCmd() {
        //System.out.println(CmdUtil.excuteCmdCommand("ping www.baidu.com"));
        //System.out.println(CmdUtil.excuteCmdCommand("shutdown -r -t 10"));
        System.out.println(CmdUtil.excuteCmdCommand("netsh wlan connect  benti-5G"));


    }


    @Test
    public void testSplitStrToList() {
        List<String> testList = StringConvertUtil.splitStrToList("1,2,3", null);
    }
}

