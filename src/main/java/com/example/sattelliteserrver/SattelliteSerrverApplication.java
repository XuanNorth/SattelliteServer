package com.example.sattelliteserrver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync

public class SattelliteSerrverApplication {

    public static void main(String[] args) {
        SpringApplication.run(SattelliteSerrverApplication.class, args);
    }

//    @Bean
//    CommandLineRunner run(SerialServerController serialServerController) {
//        return args -> {
////            new Thread(socketServerController::start).start();
//            new Thread(serialServerController::start).start();
//        };
//
//    }


}
