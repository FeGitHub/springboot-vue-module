package com.test.demo;

import java.time.LocalDate;
import java.time.YearMonth;

public class Demo {
    public static void main(String[] args) {
        int year = 2021; // 要查询的年份
        int month = 9;   // 要查询的月份（注意：这里的月份从1开始）

        YearMonth targetYearMonth = YearMonth.of(year, month);
        LocalDate lastDayOfMonth = targetYearMonth.atEndOfMonth();

        System.out.println("该月的最后一天为：" + lastDayOfMonth);
    }
}
