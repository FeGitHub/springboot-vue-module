package com.test.demo.stream;

import java.util.*;
import java.util.stream.Collectors;

//https://blog.csdn.net/MinggeQingchun/article/details/123184273
public class TestStream {
    public static void main(String[] args) {
        List<StreamUser> list = initUserList();
        testSkip(list);
    }

    public static List<StreamUser> initUserList() {
        List<StreamUser> list = new ArrayList<>();
        StreamUser user1 = new StreamUser();
        user1.setUserName("admin");
        user1.setAge(16);
        user1.setSex("男");
        StreamUser user2 = new StreamUser();
        user2.setUserName("root");
        user2.setAge(20);
        user2.setSex("女");
        StreamUser user3 = new StreamUser();
        user3.setUserName("admin");
        user3.setAge(18);
        user3.setSex("男");
        StreamUser user4 = new StreamUser();
        user4.setUserName("admin11");
        user4.setAge(22);
        user4.setSex("女");
        //添加用户到集合中
        list.add(user1);
        list.add(user2);
        list.add(user3);
        list.add(user4);
        return list;
    }


    /***
     * 測試fliter
     * 用于通过设置的条件过滤出元素
     * @param list
     */
    public static void testFliter(List<StreamUser> list) {
        /*
        在集合中查询用户名包含admin的集合
        */
        List<StreamUser> userList = list.stream().filter(user -> user.getUserName().contains("admin")
                && user.getAge() <= 20).collect(Collectors.toList());
        System.out.println(userList);

        /*
        在集合中查询出第一个用户名为admin的用户
        */
        Optional<StreamUser> user = list.stream().filter(userTemp -> "admin".equals(userTemp.getUserName())).findFirst();
        System.out.println(user);

        /*
        orElse(null)表示如果一个都没找到返回null（orElse()中可以塞默认值。如果找不到就会返回orElse中设置的默认值）
        orElseGet(null)表示如果一个都没找到返回null（orElseGet()中可以塞默认值。如果找不到就会返回orElseGet中设置的默认值）
        orElse()和orElseGet()区别：在使用方法时，即使没有值 也会执行 orElse 内的方法， 而 orElseGet则不会
        */
        //没值
        StreamUser a = list.stream().filter(userT -> userT.getAge() == 12).findFirst().orElse(getMethod("a"));
        StreamUser b = list.stream().filter(userT11 -> userT11.getAge() == 12).findFirst().orElseGet(() -> getMethod("b"));
        //有值
        StreamUser c = list.stream().filter(userT2 -> userT2.getAge() == 16).findFirst().orElse(getMethod("c"));
        StreamUser d = list.stream().filter(userT22 -> userT22.getAge() == 16).findFirst().orElseGet(() -> getMethod("d"));
        System.out.println("a：" + a);
        System.out.println("b：" + b);
        System.out.println("c：" + c);
        System.out.println("d：" + d);
    }


    /***
     * 接受一个函数作为参数。这个函数会被应用到每个元素上，并将其映射成一个新的元素（使用映射一词，是因为它和转换类似，但其中的细微差别在于它是“创建一个新版本”而不是去“修改”）
     * @param list
     */
    public static void testMap(List<StreamUser> list) {
        List<String> mapUserList = list.stream().map(user -> user.getUserName() + "用户").collect(Collectors.toList());
        mapUserList.forEach(System.out::println);
    }

    /***
     *  distinct：去重
     * 返回一个元素各异（根据流所生成元素的hashCode和equals方法实现）的流
     * @param list
     */
    public static void testDistinct(List<StreamUser> list) {
        //3、distinct：去重
        List<String> distinctUsers = list.stream().map(StreamUser::getUserName).distinct().collect(Collectors.toList());
        distinctUsers.forEach(System.out::println);
    }


    public static void testSorted(List<StreamUser> list) {
        //4、sorted：排序，根据名字倒序
        list.stream().sorted(Comparator.comparing(StreamUser::getUserName).reversed()).collect(Collectors.toList()).forEach(System.out::println);
    }

    public static void testLimit(List<StreamUser> list) {
//5、limit：取前5条数据
        list.stream().limit(5).collect(Collectors.toList()).forEach(System.out::println);
    }

    public static void testSkip(List<StreamUser> list) {
        //6、skip：跳过第几条取后几条
        list.stream().skip(7).collect(Collectors.toList()).forEach(System.out::println);
    }


    public static void testFlatMap(List<StreamUser> list) {
        //https://blog.csdn.net/u011358268/article/details/129467268
        //7、flatMap：数据拆分一对多映射
        list.stream().flatMap(user -> Arrays.stream(user.getUserName().split(","))).forEach(System.out::println);
    }

    public static void testPeek(List<StreamUser> list) {
        //8、peek：对元素进行遍历处理，每个用户ID加1输出
        list.stream().peek(user -> user.setAge(user.getAge() + 1)).forEach(System.out::println);
    }


    public static StreamUser getMethod(String name) {
        System.out.println(name + "执行了方法");
        return null;
    }
}
