package com.order.utils.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La cola topic permite que varios consumidores escuchen el mismo evento
 * topic es muy importante por es el evento que se va a escuchar
 * Para que un consumer escuche los eventos debe estar conectado al mismo exchange y escuchar el topic adecuado
 * queue permite distribuir la carga de los mensajes entre distintos consumers, los consumers con el mismo queue name
 * comparten la carga de procesamiento de mensajes, es importante que se defina el queue
 */
@Service
public class TopicPublisher {

    @Autowired
    Environment env;

    public void publish(String exchange, String topic, RabbitEvent message) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(env.getProperty("rabbit.rabbitServerUrl"));
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
