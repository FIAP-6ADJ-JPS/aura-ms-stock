package com.postech.auramsstock;

import org.springframework.boot.SpringApplication;

public class TestAuraMsStockApplication {

    public static void main(String[] args) {
        SpringApplication.from(AuraMsStockApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
