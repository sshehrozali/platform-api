package com.platform.api;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	private final static Logger logger = LoggerFactory.getLogger(ApiApplication.class);

	public static void main(String[] args) {
		logger.info("Pulumi initialised");
		SpringApplication.run(ApiApplication.class, args);
	}
}
