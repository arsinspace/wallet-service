package ru.ylab.walletservice.services.proxyAOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for logs in application
 */
@Aspect
@Component
public class LogAspect {

    @Pointcut("within(@ru.ylab.walletservice.utils.annotation.Loggable *) && execution(* *(..))")
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
