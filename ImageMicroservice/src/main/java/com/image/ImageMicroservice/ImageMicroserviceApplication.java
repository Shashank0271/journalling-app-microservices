package com.image.ImageMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ImageMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageMicroserviceApplication.class, args);
    }

}
