package com.example.springboot.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    // 切点：service 包下所有类的所有方法
    @Pointcut("execution(* com.example.springboot.service..*.*(..))")
    public void servicePointcut() {
    }

    // 前置通知：方法干活之前
    @Before("servicePointcut()")
    public void beforeLog(JoinPoint joinPoint) {
        log.info("【@Before】进入方法：{}，参数：{}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
        // 今天最重要的实验：切面看到的是"真身"
        log.info("【看真身】joinPoint.getTarget() = {}", joinPoint.getTarget().getClass().getName());
    }

    // 环绕通知：包住整个方法（最强大——能算耗时、能改返回值、能决定跑不跑）
    @Around("servicePointcut()")
    public Object aroundLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();   // 这一行才是真正调用真身的方法
            long cost = System.currentTimeMillis() - start;
            log.info("【@Around】{} 执行完成，耗时 {} ms，返回值：{}",
                    joinPoint.getSignature().getName(), cost, result);
            return result;
        } catch (Throwable e) {
            log.error("【@Around】{} 抛异常：{}", joinPoint.getSignature().getName(), e.getMessage());
            throw e;
        }
    }

    // 后置通知：方法正常返回之后
    @AfterReturning("servicePointcut()")
    public void afterReturning(JoinPoint joinPoint) {
        log.info("【@AfterReturning】{} 正常结束", joinPoint.getSignature().getName());
    }
}
