package com.ch.sagaeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class SagaEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SagaEurekaApplication.class, args);
    }

}
