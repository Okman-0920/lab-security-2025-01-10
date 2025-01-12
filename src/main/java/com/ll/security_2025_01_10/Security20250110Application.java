package com.ll.security_2025_01_10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Security20250110Application {

    public static void main(String[] args) {
        SpringApplication.run(Security20250110Application.class, args);
    }

}
