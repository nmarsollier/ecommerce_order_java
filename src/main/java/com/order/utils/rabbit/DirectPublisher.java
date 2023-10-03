package com.order.utils.rabbit;

import com.order.utils.server.Environment;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Publicar en una cola direct es enviar un mensaje directo a un destinatario en particular,
 * Necesitamos un exchange y un queue especifico para enviar correctamente el mensaje.
 * Tanto el consumer como el publisher deben compartir estos mismos datos.
 */
@Singleton
public class DirectPublisher {

    final Environment environment;

    @Inject
    public DirectPublisher(Environment environment) {
        this.environment = environment;
    }


    public void publish(String exchange, String queue, RabbitEvent message) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(environment.rabbitServerUrl());
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchange, "direct");
            channel.queueDeclare(queue, false, false, false, null);

            channel.queueBind(queue, exchange, queue);

            channel.basicPublish(exchange, queue, null, message.toJson().getBytes());

            Logger.getLogger("RabbitMQ").log(Level.INFO, "RabbitMQ Emit " + message.type);
        } catch (Exception e) {
            Logger.getLogger("RabbitMQ").log(Level.SEVERE, "RabbitMQ no se pudo encolar " + message.type, e);
        }
    }
}