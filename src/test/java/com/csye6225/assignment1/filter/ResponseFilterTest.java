package com.csye6225.assignment1.filter;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class FilterTest {

    @Test
    public void shouldSetNoCacheHeader() throws Exception {

        ResponseFilter filter = new ResponseFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (req, res) -> {};
        filter.doFilter(request, response, chain);
        assertEquals("no-cache, no-store, must-revalidate", response.getHeader("Cache-Control"));
    }

}