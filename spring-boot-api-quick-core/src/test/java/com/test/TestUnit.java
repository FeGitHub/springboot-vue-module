package com.test;

import com.test.demo.mapStruct.PersonTest;
import com.test.demo.mapStruct.PersonTestDto;
import com.test.demo.mapStruct.PersonTestMapper;
import nl.flotsam.xeger.Xeger;
import org.junit.Test;

import java.math.BigDecimal;

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


}

