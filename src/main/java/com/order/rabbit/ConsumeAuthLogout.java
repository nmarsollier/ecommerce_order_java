package com.order.rabbit;

import com.order.security.TokenService;
import com.order.utils.rabbit.FanoutConsumer;
import com.order.utils.rabbit.RabbitEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumeAuthLogout {
    @Autowired
    TokenService tokenService;

    @Autowired
    FanoutConsumer fanoutConsumer;

    public static final Logger logger = Logger.getLogger("rabbit");


    public void init() {
        fanoutConsumer.init("auth");
        fanoutConsumer.addProcessor("logout", this::logout);
        fanoutConsumer.start();
    }

    /**
     * @api {fanout} auth/logout Logout
     * @apiGroup RabbitMQ GET
     * @apiDescription Escucha de mensajes logout desde auth. Invalida sesiones en cache.
     * @apiExample {json} Mensaje
     * {
     * "type": "logout",
     * "message" : "tokenId"
     * }
     */
    public void logout(RabbitEvent event) {
        logger.log(Level.INFO, "Logout : " + event.toJson());
        tokenService.invalidate(event.message.toString());
    }
}