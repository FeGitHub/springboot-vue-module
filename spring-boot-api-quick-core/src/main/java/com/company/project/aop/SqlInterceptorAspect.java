package com.company.project.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SqlInterceptorAspect {

    // 前置通知
    @Before("@annotation(interceptSql)")
    public void beforeAdvice(JoinPoint joinPoint, InterceptSql interceptSql) {
        System.out.println("执行 SQL 之前");

        // 获取目标对象及方法名称
        Object target = joinPoint.getTarget();
        String methodName = joinPoint.getSignature().getName();

        System.out.println("目标对象：" + target);
        System.out.println("方法名称：" + methodName);
    }

    // 后置返回通知
    @AfterReturning("@annotation(interceptSql)")
    public void afterReturningAdvice(JoinPoint joinPoint, InterceptSql interceptSql) {
        System.out.println("执行 SQL 成功");
    }
}
