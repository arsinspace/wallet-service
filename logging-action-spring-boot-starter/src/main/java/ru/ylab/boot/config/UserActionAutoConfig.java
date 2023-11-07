package ru.ylab.boot.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.ylab.boot.aop.UserActionAspect;
import ru.ylab.boot.dao.UserActionDAO;
import ru.ylab.boot.repository.UserActionRepository;

/**
 * This configuration create beans for working with UserActionAspect and contains bean UserActionRepository
 * config after JdbcTemplate because need it for UserActionRepository bean
 */
@Configuration
@ConditionalOnClass(UserActionAspect.class)
@AutoConfigureAfter(JdbcTemplate.class)
public class UserActionAutoConfig {
    /**
     * Bean for UserActionRepository, conditional on bean JdbcTemplate
     * @param jdbcTemplate JdbcTemplate
     * @return UserActionDAO
     */
    @Bean
    @ConditionalOnBean(JdbcTemplate.class)
    public UserActionRepository userActionDAO(JdbcTemplate jdbcTemplate){
        return new UserActionDAO(jdbcTemplate);
    }

    /**
     * UserActionAspect bean
     * @param userActionDAO UserActionRepository
     * @return UserActionAspect bean
     */
    @Bean
    public UserActionAspect userActionAspect(UserActionRepository userActionDAO){
        return new UserActionAspect(userActionDAO);
    }
}
