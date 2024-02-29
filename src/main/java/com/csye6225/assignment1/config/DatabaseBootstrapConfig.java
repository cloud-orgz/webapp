package com.csye6225.assignment1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class DatabaseBootstrapConfig {

    @Autowired
    private DataSource dataSource;

    @Value("${DB_NAME}") // This line injects the DB_NAME value from your .env file
    private String dbName;

    @PostConstruct
    public void createDatabase() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = String.format("CREATE DATABASE IF NOT EXISTS %s", dbName); // Use the dbName variable
        jdbcTemplate.execute(sql);
    }
}

