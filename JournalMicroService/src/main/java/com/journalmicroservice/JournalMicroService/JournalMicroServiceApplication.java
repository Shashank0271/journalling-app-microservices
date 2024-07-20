package com.journalmicroservice.JournalMicroService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class JournalMicroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JournalMicroServiceApplication.class, args);
    }

}
