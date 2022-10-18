package com.company.project.aop;

import com.company.project.core.Result;
import com.company.project.core.ServiceException;
import com.company.project.utils.ValidationUtil;
import com.company.project.vo.base.RequestVo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/***
 *  请求实体参数aop检验
 */
@Component
@Aspect
public class ValidationAop {

    /***
     * 只对控制器进行切面
     */
    @Pointcut("execution(* com.company.project.web..*.*(..))")
    private void pointCut() {
    }

    @Around("pointCut()")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        Class<?> returnType = ((MethodSignature) signature).getReturnType();
        if (Result.class.isAssignableFrom(returnType)) {
            for (Object object : pjp.getArgs()) {
                if (object instanceof RequestVo) {
                    RequestVo requestVo = (RequestVo) object;
                    ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(requestVo);
                    if (validResult.hasErrors()) {
                        String errors = validResult.getErrors();
                        throw new ServiceException(errors);
                    }
                }
            }
        }
        return pjp.proceed();
    }
}
