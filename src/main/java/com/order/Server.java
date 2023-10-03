package com.order;

import com.order.rabbit.RabbitController;
import com.order.rest.Routes;
import com.order.utils.server.Environment;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    final Environment environment;

    final Routes routes;

    final RabbitController rabbitController;

    @Inject
    public Server(
            Environment environment,
            Routes routes,
            RabbitController rabbitController
    ) {
        this.environment = environment;
        this.routes = routes;
        this.rabbitController = rabbitController;
    }

    public static void main(String[] args) {
        DaggerServerComponent.builder().build().getServer().start();
    }

    void start() {
        routes.init();
        rabbitController.init();

        Logger.getLogger("Validator").log(Level.INFO,
                "Order Service escuchando en el puerto : " + environment.serverPort());
    }
}
