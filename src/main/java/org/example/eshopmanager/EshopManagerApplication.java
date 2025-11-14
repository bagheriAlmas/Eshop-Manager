package org.example.eshopmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EshopManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopManagerApplication.class, args);
    }

}
