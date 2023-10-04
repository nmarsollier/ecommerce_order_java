package com.order;

import com.order.rabbit.RabbitController;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {
    @Autowired
    RabbitController rabbitController;


    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    @PostConstruct
    void init() {
        rabbitController.init();
    }
}
