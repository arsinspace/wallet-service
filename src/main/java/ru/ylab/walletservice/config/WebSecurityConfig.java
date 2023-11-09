package ru.ylab.walletservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.ylab.walletservice.in.filter.WebSecurityFilter;
import ru.ylab.walletservice.repository.UserRepository;
import ru.ylab.walletservice.utils.security.JWTTokenProvider;

import java.util.List;

/**
 * Configuration class responsible for defining and configuring the security
 * settings for the application.
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserRepository userDAO;
    private static final List<String> SWAGGER_V3_PATHS = List.of("/swagger-ui**/**", "/v3/api-docs**/**");

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain springWebFilterChain(HttpSecurity http, JWTTokenProvider jwtTokenProvider) throws Exception {

        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"auth/login")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST,"auth/signup")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST,"transaction/credit")
                                .hasAuthority("ROLE_USER")
                                .requestMatchers(HttpMethod.GET,"wallet/get-balance")
                                .hasAuthority("ROLE_USER")
                                .requestMatchers(HttpMethod.POST,"transaction/debit")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"transaction/history")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET,"admin/**")
                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(SWAGGER_V3_PATHS.get(0)).permitAll()
                        .requestMatchers(SWAGGER_V3_PATHS.get(1)).permitAll()
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
        return authentication -> {
            String username = authentication.getPrincipal() + "";
            String password = authentication.getCredentials() + "";
            UserDetails user = userDetailsService.loadUserByUsername(username);
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
