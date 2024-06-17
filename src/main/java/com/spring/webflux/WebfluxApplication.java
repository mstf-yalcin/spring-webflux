package com.spring.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@SpringBootApplication
@Slf4j
@EnableR2dbcAuditing
public class WebfluxApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
    }

    @Override
    public void run(String... args) {
    }
}
