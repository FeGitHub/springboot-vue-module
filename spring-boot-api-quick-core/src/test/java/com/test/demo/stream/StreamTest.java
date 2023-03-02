package com.test.demo.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamTest {

    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        /*使用函数式编程*/
        /* 第一种方式*/
        List<Integer> listAdd = list.stream().map(s -> s + 2).collect(Collectors.toList());
        System.out.println("listAdd" + listAdd);
        /* 第二种方式*/
        List<Integer> listAdd02 = list.stream().map(StreamTest::add2).collect(Collectors.toList());
        System.out.println("listAdd02" + listAdd02);
    }

    /*声明一个方法，加2，并返回结果*/
    private static int add2(Integer temp) {
        return temp + 2;
    }


}
