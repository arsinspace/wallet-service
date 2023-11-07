package ru.ylab.boot.utills.annotation;

import org.springframework.context.annotation.Import;
import ru.ylab.boot.config.LoggingAutoconfigure;

import java.lang.annotation.*;

/**
 * Annotation for enable LoggingAutoconfigure
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(LoggingAutoconfigure.class)
@Documented
public @interface EnableLoggingAspect {
}
