package com.order.rabbit;

import com.order.events.EventService;
import com.order.rabbit.dto.NewArticleValidationData;
import com.order.utils.rabbit.DirectConsumer;
import com.order.utils.rabbit.RabbitEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ConsumeArticleData {
    @Autowired
    DirectConsumer directConsumer;

    @Autowired
    EventService eventService;

    public static final Logger logger = Logger.getLogger("rabbit");


    public void init() {
        directConsumer.init("order", "order");
        directConsumer.addProcessor("article-data", this::articleData);
        directConsumer.start();
    }

    /**
     * @api {direct} order/article-data Validar Artículos
     * @apiGroup RabbitMQ GET
     * @apiDescription Antes de iniciar las operaciones se validan los artículos contra el catalogo.
     * @apiExample {json} Mensaje
     * {
     * "type": "article-data",
     * "message" : {
     * "cartId": "{cartId}",
     * "articleId": "{articleId}",
     * "valid": True|False
     * }
     * }
     */
    public void articleData(RabbitEvent event) {
        NewArticleValidationData articleExist = NewArticleValidationData.fromJson(event.message.toString());
        try {
            eventService.placeArticleExist(articleExist);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}