//package com.mjc.school;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//
//@EnableWebMvc
//@SpringBootApplication
//@EntityScan(basePackages = "com.mjc.school.repository")
//public class Main {
//
//    public static void main(String[] args) {
//        SpringApplication.run(Main.class, args);
//    }
//
//}


package com.mjc.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@EnableWebMvc
@SpringBootApplication
@EntityScan(basePackages = "com.mjc.school.repository")
public class Main extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Main.class);
    }
}
