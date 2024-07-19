package com.nhnacademy.codequestweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class CodeQuestWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(CodeQuestWebApplication.class, args);
    }
}