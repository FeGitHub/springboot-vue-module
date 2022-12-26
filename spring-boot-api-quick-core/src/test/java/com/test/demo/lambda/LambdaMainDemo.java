package com.test.demo.lambda;

public class LambdaMainDemo {
    public static void main(String[] args) {
        // 调用invokeCook()方法 传递Cook接口的匿名内部类对象
        invokeDo(new LambdaInterface() {
            @Override
            public void doSomething() {
                System.out.println("doSomething");
            }
        });

        // 使用Lambda表达式简化匿名内部类的书写
        invokeDo(() -> {
            System.out.println("doSomething");
        });
    }

    public static void invokeDo(LambdaInterface lambdaInterface) {
        lambdaInterface.doSomething();
    }
}
