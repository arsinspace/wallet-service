package ru.ylab.walletservice.config;

import org.springframework.context.annotation.Configuration;
import ru.ylab.boot.utills.annotation.EnableLoggingAspect;

/**
 * This configuration enable logging-spring-boot-starter and create LogAspect bean
 */
@Configuration
@EnableLoggingAspect
public class LoggingAspectInitConfig {
}
