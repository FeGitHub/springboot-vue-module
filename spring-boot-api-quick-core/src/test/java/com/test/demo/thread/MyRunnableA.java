package com.test.demo.thread;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

// https://blog.csdn.net/weixin_28758387/article/details/120321000?spm=1001.2101.3001.6650.18&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-18-120321000-blog-122908112.235%5Ev36%5Epc_relevant_default_base3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-18-120321000-blog-122908112.235%5Ev36%5Epc_relevant_default_base3&utm_relevant_index=26
class MyRunnableA<T> implements Runnable {
    // 使用result作为返回值的存储变量，使用volatile修饰防止指令重排
    private volatile Result<T> result;
    private String name;

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void run() {
        // 因为在这个过程中会对result进行赋值，保证在赋值时外部线程不能获取，所以加锁
        synchronized (this) {
            try {
                System.out.println(LocalDateTime.now() + " run方法正在执行");
                TimeUnit.SECONDS.sleep(7);
                result = new Result("这是返回结果:" + this.name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 赋值结束后唤醒等待线程
                this.notifyAll();
            }
        }
    }

    // 方法加锁，只能有一个线程获取
    public synchronized Result<T> getResult() throws InterruptedException {
        // 循环校验是否已经给结果赋值
        while (result == null) {
            // 如果没有赋值则等待
            this.wait();
        }
        return result;
    }

    // 使用内部类包装结果而不直接使用T作为返回结果
    // 可以支持返回值等于null的情况
    static class Result<T> {
        T value;

        public Result(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }
}

