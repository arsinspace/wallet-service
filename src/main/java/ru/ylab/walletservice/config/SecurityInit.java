package ru.ylab.walletservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This class makes Spring Security enabled
 */
public class SecurityInit extends AbstractSecurityWebApplicationInitializer {

}
