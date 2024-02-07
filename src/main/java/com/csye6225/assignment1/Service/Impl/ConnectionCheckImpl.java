package com.csye6225.assignment1.Service.Impl;

import com.csye6225.assignment1.Service.ConnectionCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Service
public class ConnectionCheckImpl implements ConnectionCheck {

    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public boolean checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(1000);
        } catch (Exception e) {
            return false;
        }
    }
}
