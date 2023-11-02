package ru.ylab.walletservice.services.proxyAOP;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import ru.ylab.walletservice.dao.UserActionDAO;
import ru.ylab.walletservice.utils.annotation.TrackEvent;

import java.sql.Timestamp;

/**
 * This aspect takes the associated parameters in the user action method and writes them to a database table
 */
@Aspect
@Component
@ComponentScan(value = "ru.ylab.walletservice.dao")
@RequiredArgsConstructor
public class UserActionAspect {

    private final UserActionDAO userActionDAO;
    /**
     * Execute on annotation TrackEvent
     * @param joinPoint JoinPoint
     * @throws Throwable
     */
    @Around("execution(@ru.ylab.walletservice.utils.annotation.TrackEvent * *(..))")
    public Object trackEvent(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        TrackEvent trackEvent = signature.getMethod().getAnnotation(TrackEvent.class);
        System.out.println(trackEvent);
        String eventName = trackEvent.action();

        Object result = joinPoint.proceed();
        System.out.println(result);
        userActionDAO.saveUserAction(0,eventName, result.toString(),
                new Timestamp(System.currentTimeMillis()));

        return result;
    }
}
