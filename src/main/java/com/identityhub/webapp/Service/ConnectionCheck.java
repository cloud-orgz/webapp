package com.identityhub.webapp.Service;

import org.springframework.stereotype.Service;

@Service
public interface ConnectionCheck {
    public boolean checkDatabaseConnection();
}
