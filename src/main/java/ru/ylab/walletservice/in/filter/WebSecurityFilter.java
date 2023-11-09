package ru.ylab.walletservice.in.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import ru.ylab.walletservice.repository.UserRepository;
import ru.ylab.walletservice.utils.security.JWTTokenProvider;

import java.io.IOException;

/**
 * This is a custom filter registered with the spring
 * security filter chain and works in conjunction with the security
 * configuration
 * It is responsible for verifying the authenticity of incoming HTTP requests to
 * secured API endpoints by examining JWT token in the request header, verifying
 * it is signature, expiration and evaluating it is presence in the token revocation list.
 * If authentication is successful, the filter populates the security context with
 * the user's unique identifier and the permissions associated with the
 * authenticated user which can be referenced by the application later.
 * This filter is only executed for secure endpoints, and is skipped if the incoming
 * request is destined to a non-secured public API endpoint.
 */
@Component
@RequiredArgsConstructor
public class WebSecurityFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(WebSecurityFilter.class);
    /**
     * This field contains HEADER_PREFIX for auth
     */
    public static final String HEADER_PREFIX = "Bearer ";
    /**
     * This field contains link to JWTTokenProvider
     */
    private final JWTTokenProvider jwtTokenProvider;
    /**
     * This field contains link to UserRepository
     */
    private final UserRepository userDAO;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) req);
        LOG.info("Extracting token from HttpServletRequest: {}", token);

        if (token != null && jwtTokenProvider.validateToken(token)) {

            Authentication auth = jwtTokenProvider.getAuthentication(token);
            long userId = userDAO.findUserIdByUsername(auth.getName()).get();
            req.setAttribute("userId", userId);

            if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
                System.out.println("AnonymousAuthenticationToken");
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(auth);
                SecurityContextHolder.setContext(context);
            }
        }
        filterChain.doFilter(req, res);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
