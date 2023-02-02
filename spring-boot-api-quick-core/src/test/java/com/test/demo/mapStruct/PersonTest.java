package com.test.demo.mapStruct;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class PersonTest {

    String describe;

    private String id;

    private String name;

    private int age;

    private BigDecimal source;

    private double height;

    private Date createTime;

    private Boolean testBoolean1;

    private boolean testBoolean2;

    private List<String> testList;


}



