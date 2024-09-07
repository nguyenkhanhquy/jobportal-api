package com.jobportal.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobportalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobportalApiApplication.class, args);
    }

}
