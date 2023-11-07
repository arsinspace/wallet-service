package ru.ylab.boot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ylab.boot.aop.LogAspect;
/**
 * This configuration create beans for working with LoggingAspect
 * config Conditional on class LogAspect
 */
@Configuration
@ConditionalOnClass(LogAspect.class)
public class LoggingAutoconfigure {
    /**
     * Bean LogAspect
     * @return LogAspect bean
     */
    @Bean
    public LogAspect logAspect(){
        return new LogAspect();
    }
}
