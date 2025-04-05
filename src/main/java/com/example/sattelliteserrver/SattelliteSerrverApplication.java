package com.example.sattelliteserrver;

import com.example.sattelliteserrver.socket.SocketServerController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync

public class SattelliteSerrverApplication {

    public static void main(String[] args) {
        SpringApplication.run(SattelliteSerrverApplication.class, args);
    }

    @Bean
    CommandLineRunner run(SocketServerController socketServerController) {
        return args -> {
            new Thread(socketServerController::start).start();
        };
    }

}
