package com.palantir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class PalantirApplication {

    public static void main(String[] args) {
        SpringApplication.run(PalantirApplication.class, args);
    }

}
