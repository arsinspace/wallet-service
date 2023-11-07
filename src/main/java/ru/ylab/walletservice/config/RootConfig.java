package ru.ylab.walletservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
/**
 * Configuration class responsible for defining configuring import root config webmvc to application
 */
@ComponentScan
@Configuration
@Import({WebSecurityConfig.class, AspectConfig.class})
public class RootConfig {
}
