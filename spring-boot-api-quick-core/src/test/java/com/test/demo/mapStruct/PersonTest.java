package com.test.demo.mapStruct;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PersonTest {

    String describe;

    private String id;

    private String name;

    private int age;

    private BigDecimal source;

    private double height;

    private Date createTime;


}



