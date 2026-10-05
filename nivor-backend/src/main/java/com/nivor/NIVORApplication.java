package com.nivor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NIVORApplication {
    public static void main(String[] args) {
        SpringApplication.run(NIVORApplication.class, args);
    }
}
