package com.ai4ceo.web;

import com.ai4ceo.web.config.DatabaseUrlSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringLoginWebApplication {

	public static void main(String[] args) {
		DatabaseUrlSupport.applyFromEnv();
		SpringApplication.run(SpringLoginWebApplication.class, args);
	}

}
