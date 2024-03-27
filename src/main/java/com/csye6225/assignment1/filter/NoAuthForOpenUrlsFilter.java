package com.csye6225.assignment1.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@Order(4)
@WebFilter("/*")
public class NoAuthForOpenUrlsFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(NoAuthForOpenUrlsFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;


        String path = httpRequest.getRequestURI();
        boolean isAuthHeaderPresent = httpRequest.getHeader("Authorization") != null;


        // Check for Authorization header in request to open URLs
        if (("/healthz".equals(path) || "/v1/user".equals(path) || "/v1/user/verify".equals(path)) && isAuthHeaderPresent) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
