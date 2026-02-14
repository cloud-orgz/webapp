package com.identityhub.webapp.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class NoRequestBodyFilterTest {

    @Test
    public void shouldReturnBadRequestWhenRequestBodyIsPresent() throws Exception {

        NoRequestBodyFilter filter = new NoRequestBodyFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        String requestBody = "zavnonwgjoeberohit";
        request.setContent(requestBody.getBytes(StandardCharsets.UTF_8));

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = (req, res) -> {};


        filter.doFilter(request, response, chain);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    @Test
    public void shouldContinueFilterChainWhenNoRequestBodyIsPresent() throws Exception {

        NoRequestBodyFilter filter = new NoRequestBodyFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();

        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setMethod("GET");
        final String filterChainExecutedAttribute = "filterChainExecuted";
        FilterChain chain = (req, res) -> req.setAttribute(filterChainExecutedAttribute, true);

        filter.doFilter(request, response, chain);

        assertEquals(true, request.getAttribute(filterChainExecutedAttribute));
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }
}
