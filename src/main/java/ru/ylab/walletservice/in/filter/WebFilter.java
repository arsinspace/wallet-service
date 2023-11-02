package ru.ylab.walletservice.in.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ylab.walletservice.out.response.SendResponse;

import java.io.IOException;
import java.util.Arrays;

/**
 * This WebFilter contains filter logic and send request to next resource
 */
@jakarta.servlet.annotation.WebFilter(urlPatterns = "/*")
public class WebFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String servletName = request.getHttpServletMapping().getServletName();

        if (servletName.equals("SignupServlet") || servletName.equals("LoginServlet")) {
            filterChain.doFilter(servletRequest, servletResponse);

        } else if (searchCookies(request, "jwt")) {

            Arrays.stream(((HttpServletRequest) servletRequest).getCookies())
                    .filter(el -> el.getName().equals("jwt"))
                    .map(Cookie::getValue)
                    .forEach(System.out::println);
            System.out.println("ok");
            filterChain.doFilter(servletRequest, servletResponse);

        } else if (servletName.equals("AdminActionsServlet") && searchCookies(request,"admin")){
            filterChain.doFilter(servletRequest, servletResponse);

        } else {
            SendResponse.getInstance().sendResponse((HttpServletResponse) servletResponse,
                    HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    /**
     * Method search valid cookies in user browser
     * @param request HttpServletRequest
     * @param cookieName String
     * @return boolean result of search
     */
    private static boolean searchCookies(HttpServletRequest request, String cookieName){
        return request != null && request.getCookies() != null && Arrays.stream(request.getCookies()).anyMatch(
                el -> el.getName().equals(cookieName));
    }
}