package com.csye6225.assignment1.controller;

import com.csye6225.assignment1.Service.ConnectionCheck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(Health.class)
class HealthApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConnectionCheck service;

    @Test
    public void shouldReturn200WhenDatabaseIsConnected() throws Exception {
        given(service.checkDatabaseConnection()).willReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/healthz"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturn503WhenDatabaseIsNotConnected() throws Exception {
        given(service.checkDatabaseConnection()).willReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.get("/healthz"))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    public void shouldReturn405WhenTriedWithIncorrectedMethod() throws Exception {
        given(service.checkDatabaseConnection()).willReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/healthz"))
                .andExpect(status().isMethodNotAllowed());
    }
}