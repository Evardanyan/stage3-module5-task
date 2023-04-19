package com.mjc.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
@EntityScan(basePackages = "com.mjc.school.repository")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
