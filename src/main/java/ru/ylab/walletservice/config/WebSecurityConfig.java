package ru.ylab.walletservice.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import ru.ylab.walletservice.in.filter.WebSecurityFilter;
import ru.ylab.walletservice.out.response.SendResponse;
import ru.ylab.walletservice.repository.UserRepository;
import ru.ylab.walletservice.utils.security.JWTTokenProvider;

import java.util.List;

/**
 * Configuration class responsible for defining and configuring the security
 * settings for the application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@ComponentScan(value = {"ru.ylab.walletservice.utils.security","ru.ylab.walletservice.dao"})
public class WebSecurityConfig {

    private final UserRepository userDAO;
    private static final List<String> SWAGGER_V3_PATHS = List.of("/swagger-ui**/**", "/v3/api-docs**/**");
    @Bean(name = "mvcHandlerMappingIntrospector")
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain springWebFilterChain(HttpSecurity http, JWTTokenProvider jwtTokenProvider
            , HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder =
                new MvcRequestMatcher.Builder(introspector);

        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                            new SendResponse().sendResponse(response, HttpServletResponse.SC_BAD_REQUEST,
                                    "You are here")
                        ))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST,"auth/login"))
                                .permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST,"auth/signup"))
                                .permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST,"transaction/credit"))
                                .hasAuthority("ROLE_USER")
                                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST,"transaction/debit"))
                                .permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET,"transaction/history"))
                                .permitAll()
                                .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET,"admin/**"))
                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(mvcMatcherBuilder.pattern(SWAGGER_V3_PATHS.get(1))).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern(SWAGGER_V3_PATHS.get(2))).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new WebSecurityFilter(jwtTokenProvider, userDAO),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public UserDetailsService customUserDetailsService() {
        return (username) -> userDAO.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
    }

    @Bean
    public AuthenticationManager customAuthenticationManager(UserDetailsService userDetailsService,
                                                             PasswordEncoder encoder) {
        System.out.println("AuthenticationManager");
        System.out.println(userDetailsService.toString());
        return authentication -> {
            String username = authentication.getPrincipal() + "";
            String password = authentication.getCredentials() + "";
            System.out.println(username + " " + password);
            UserDetails user = userDetailsService.loadUserByUsername(username);
            System.out.println(user.getPassword());
            if (!encoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Bad credentials");
            }
            System.out.println(encoder.encode(password));
            if (!user.isEnabled()) {
                throw new DisabledException("User account is not active");
            }

            return new UsernamePasswordAuthenticationToken(username,
                    null, user.getAuthorities());
        };
    }
}
