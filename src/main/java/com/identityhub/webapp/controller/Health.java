package com.identityhub.webapp.controller;

import com.identityhub.webapp.Service.ConnectionCheck;
import com.identityhub.webapp.filter.NoRequestBodyFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health {

    @Autowired
    ConnectionCheck service;

    private static final Logger logger = LoggerFactory.getLogger(NoRequestBodyFilter.class);

    @GetMapping(path = "/healthz")
    public ResponseEntity<Void> healthApi(){
        if(service.checkDatabaseConnection()) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }else{
            logger.warn("Service Unavailable");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }
}
