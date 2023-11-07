package ru.ylab.boot.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Aspect for logs in application
 */
@Aspect
public class LogAspect {

    @Pointcut("@annotation(ru.ylab.boot.utills.annotation.Loggable)")
    public void loggableAnnotated(){

    }

    @Around("loggableAnnotated()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        System.out.println("Calling method" + proceedingJoinPoint.getSignature());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("Execution of method " + proceedingJoinPoint.getSignature() +
                " finished. Execution time is " + (endTime - startTime) + " ms");
        return result;
    }
}
