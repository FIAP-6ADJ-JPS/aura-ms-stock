package com.postech.auramsstock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AuraMsStockApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuraMsStockApplication.class, args);
    }

}
