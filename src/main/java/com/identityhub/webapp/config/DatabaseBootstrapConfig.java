package com.identityhub.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class DatabaseBootstrapConfig {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void createDatabase() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("CREATE DATABASE IF NOT EXISTS cloud");
    }
}

