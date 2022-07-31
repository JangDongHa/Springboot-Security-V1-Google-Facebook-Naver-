package com.example.securitydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SecuritydemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecuritydemoApplication.class, args);
    }

}
