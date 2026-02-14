package com.identityhub.webapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Order(3)
@WebFilter("/healthz")
public class NoRequestBodyFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(NoRequestBodyFilter.class);
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String body = StreamUtils.copyToString(httpRequest.getInputStream(), StandardCharsets.UTF_8);
        if(!httpRequest.getMethod().equalsIgnoreCase("GET")){
            logger.warn("Only get Method is allowed: {}", httpRequest.getRequestURI());
            httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }if (!body.isEmpty()&&httpRequest.getMethod().equalsIgnoreCase("GET")) {
            logger.warn("Request with Body received: {}", httpRequest.getRequestURI());
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        filterChain.doFilter(servletRequest, servletResponse);
    }
}
