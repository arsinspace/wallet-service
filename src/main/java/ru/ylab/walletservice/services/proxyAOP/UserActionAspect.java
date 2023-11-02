package ru.ylab.walletservice.services.proxyAOP;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import ru.ylab.walletservice.dao.UserActionDAO;
import ru.ylab.walletservice.utils.annotation.TrackEvent;
import ru.ylab.walletservice.utils.annotation.TrackParameter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * This aspect takes the associated parameters in the user action method and writes them to a database table
 */
@Aspect
public class UserActionAspect {
    /**
     * Execute on annotation TrackEvent
     * @param joinPoint JoinPoint
     * @throws Throwable
     */
    @Around("execution(@ru.ylab.walletservice.ulits.annotation.TrackEvent  * *(..))")
    public void trackEvent(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        TrackEvent trackEvent = signature.getMethod().getAnnotation(TrackEvent.class);

        String eventName = trackEvent.action();

        Map<String, String> params = new HashMap<>();
        params.put("eventName", eventName);

        Annotation[][] parameterAnnotations = signature.getMethod().getParameterAnnotations();

        if (parameterAnnotations.length != 0) {
            int i = 0;
            for (Annotation[] parameterAnnotation : parameterAnnotations) {
                for (Annotation annotation : parameterAnnotation) {
                    if (annotation instanceof TrackParameter) {
                        String key = ((TrackParameter) annotation).value();
                        params.put(key, (String) joinPoint.getArgs()[i++]);
                    }
                }
            }
        }

        UserActionDAO.getInstance().saveUserAction(Long.parseLong(params.get("userId"))
                ,params.get("action"));

        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
