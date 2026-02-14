package com.identityhub.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class IdentityHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentityHubApplication.class, args);
	}

}