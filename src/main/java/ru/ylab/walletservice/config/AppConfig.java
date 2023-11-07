package ru.ylab.walletservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
/**
 * Configuration class responsible for defining and configuring the application
 * settings.
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
        PropertySourcesPlaceholderConfigurer ppc =
                new PropertySourcesPlaceholderConfigurer();

        ppc.setLocation(new ClassPathResource("application.yml"));
        return ppc;
    }
}
