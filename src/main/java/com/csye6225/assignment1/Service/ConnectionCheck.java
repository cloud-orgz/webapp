package com.csye6225.assignment1.Service;

import org.springframework.stereotype.Service;

@Service
public interface ConnectionCheck {
    public boolean checkDatabaseConnection();
}
