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
 * La cola topic permite que varios consumidores escuchen el mismo evento
 * topic es muy importante por es el evento que se va a escuchar
 * Para que un consumer escuche los eventos debe estar conectado al mismo exchange y escuchar el topic adecuado
 * queue permite distribuir la carga de los mensajes entre distintos consumers, los consumers con el mismo queue name
 * comparten la carga de procesamiento de mensajes, es importante que se defina el queue
 */
@Singleton
public class TopicPublisher {

    final Environment environment;

    @Inject
    public TopicPublisher(Environment environment) {
        this.environment = environment;
    }


    public void publish(String exchange, String topic, RabbitEvent message) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(environment.rabbitServerUrl());
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchange, "topic");

            channel.basicPublish(exchange, topic, null, message.toJson().getBytes());

            Logger.getLogger("RabbitMQ").log(Level.INFO, "RabbitMQ Emit " + message.type);
        } catch (Exception e) {
            Logger.getLogger("RabbitMQ").log(Level.SEVERE, "RabbitMQ no se pudo encolar " + message.type, e);
        }
    }
}
