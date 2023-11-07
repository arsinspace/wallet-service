package ru.ylab.walletservice.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
/**
 * Configuration class responsible for defining start the application.
 */
@ComponentScan(basePackages = {"org.springdoc"})
public class AppInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {
            RootConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {
                WebConfig.class, OpenApiConfig.class,org.springdoc.core.SpringDocConfiguration.class,
                org.springdoc.webmvc.core.SpringDocWebMvcConfiguration.class,
                org.springdoc.webmvc.ui.SwaggerConfig.class,
                org.springdoc.core.SwaggerUiConfigProperties.class,
                org.springdoc.core.SwaggerUiOAuthProperties.class,
                org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

}
