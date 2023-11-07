package ru.ylab.boot.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import ru.ylab.boot.repository.UserActionRepository;
import ru.ylab.boot.utils.annotation.TrackEvent;

import java.sql.Timestamp;

/**
 * This aspect takes the associated parameters in the user action method and writes them to a database table
 */
@Aspect
@RequiredArgsConstructor
public class UserActionAspect {
    /**
     * This field contains link to UserActionRepository
     */
    private final UserActionRepository userActionDAO;

    /**
     * Execute on annotation TrackEvent
     * @param joinPoint JoinPoint
     * @throws Throwable Exception
     */
    @Around("execution(@ru.ylab.boot.utils.annotation.TrackEvent * *(..))")
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
