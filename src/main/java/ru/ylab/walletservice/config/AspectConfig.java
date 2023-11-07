package ru.ylab.walletservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration for AspectJAutoProxy
 */
@Configuration
@ComponentScan(value = "ru.ylab.walletservice.services.proxyAOP")
@EnableAspectJAutoProxy
public class AspectConfig {
}
