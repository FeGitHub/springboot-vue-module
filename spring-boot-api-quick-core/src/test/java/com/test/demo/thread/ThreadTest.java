package com.test.demo.thread;

import lombok.SneakyThrows;

public class ThreadTest {

    public static void main(String[] args) {
        //继承Thread类方式　　　　//此处的new Thread()方法整体就是匿名内部类，创建线程对象，可以直接重写run()方法
        new Thread() {
            @SneakyThrows
            public void run() {
                for (int i = 0; i < 100; i++) {
                    // Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + ":" + i);
                }
            }
        }.start();
        //实现Runnable接口方式　　　　//此处的Runnable r=new Thread()方法整体就是匿名内部类，重写Runnable中的run()方法
        Runnable r = new Runnable() {
            @SneakyThrows
            public void run() {
                for (int i = 0; i < 100; i++) {
                    //  Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + ":" + i);
                }
            }
        };
        //创建线程对象并开启线程
        new Thread(r).start();
    }
}
