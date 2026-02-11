package com.ch.sagaconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class SagaConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(SagaConfigApplication.class, args);
    }

}
