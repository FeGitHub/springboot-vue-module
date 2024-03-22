package com.company.project.Interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SqlExecutionInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object result = null;
        if (invocation.getMethod().getName().startsWith("execute")) {
            Connection connection = (Connection) invocation.getThis();
            String sql = (String) invocation.getArguments()[0];
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.execute();
            System.out.println(sql); // 在日志中打印SQL语句
        }
        result = invocation.proceed();
        return result;
    }
}
