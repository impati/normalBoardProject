package com.example.normalboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class NormalBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(NormalBoardApplication.class, args);
    }

}
