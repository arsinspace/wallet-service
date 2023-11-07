package ru.ylab.walletservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for OpenApi
 */
@Configuration
@EnableWebMvc
public class OpenApiConfig implements WebMvcConfigurer {

    /**
     * Create GroupedOpenApi bean
     * @return GroupedOpenApi
     */
    @Bean
    public GroupedOpenApi dafault() {
        return GroupedOpenApi.builder()
                .group("all")
                .packagesToScan("ru.ylab.walletservice")
                .pathsToMatch("/rest/**").build();
    }

    /**
     * Create OpenAPI bean
     * @return OpenAPI
     */
    @Bean
    public OpenAPI ApiInfo() {
        return new OpenAPI()
                .info(new Info().title("Wallet - Service")
                        .description("The service helps in creating a user with wallet " +
                                "(default balance = 0.0), credit funds to the wallet, deposit funds from wallet," +
                                " maintain transaction details, login as Admin and view all users and users actions")
                        .version("0.0.X SUPER_AGENT")
                        .contact(new Contact().name("arsinspace").email("@email").url("myUrl"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                        .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("Bearer")).addSecuritySchemes("Refresh Token Header",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Refresh Token Header"))
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication")
                        .addList("Refresh Token Header"));
    }
}
