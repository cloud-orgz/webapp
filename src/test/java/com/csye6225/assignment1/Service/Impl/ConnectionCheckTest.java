package com.csye6225.assignment1.Service.Impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ConnectionCheckTest {
    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @InjectMocks
    private ConnectionCheckImpl connectionCheck;

    @Test
    public void shouldReturnTrueWhenDatabaseConnectionIsValid() throws SQLException {
        // Simulate a valid database connection
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1000)).thenReturn(true);
        boolean result = connectionCheck.checkDatabaseConnection();
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenDatabaseConnectionIsInvalid() throws SQLException {
        // Simulate an invalid database connection
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1000)).thenReturn(false);
        boolean result = connectionCheck.checkDatabaseConnection();
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseWhenDatabaseConnectionThrowsException() throws SQLException {
        // Simulate an exception when trying to get a connection
        when(dataSource.getConnection()).thenThrow(SQLException.class);
        boolean result = connectionCheck.checkDatabaseConnection();
        assertFalse(result);
    }

}