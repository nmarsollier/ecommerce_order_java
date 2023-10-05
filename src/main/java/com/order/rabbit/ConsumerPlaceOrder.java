package com.order.rabbit;

import com.google.gson.annotations.SerializedName;
import com.order.events.EventService;
import com.order.events.schema.Event;
import com.order.events.schema.PlaceEvent;
import com.order.rabbit.dto.NewPlaceData;
import com.order.security.TokenService;
import com.order.utils.rabbit.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ConsumerPlaceOrder {
    @Autowired
    TokenService tokenService;

    @Autowired
    FanoutConsumer fanoutConsumer;

    @Autowired
    DirectConsumer directConsumer;

    @Autowired
    TopicPublisher topicPublisher;

    @Autowired
    DirectPublisher directPublisher;

    @Autowired
    EventService eventService;

    public static final Logger logger = Logger.getLogger("rabbit");


    public void init() {
        directConsumer.init("order", "order");
        directConsumer.addProcessor("place-order", this::placeOrder);
        directConsumer.start();
    }

    /**
     * @api {direct} order/place-order Crear Orden
     * @apiGroup RabbitMQ GET
     * @apiDescription Escucha de mensajes place-order en el canal de order.
     * @apiExample {json} Mensaje
     * {
     * "type": "place-order",
     * "exchange" : "{Exchange name to reply}"
     * "queue" : "{Queue name to reply}"
     * "message" : {
     * "cartId": "{cartId}",
     * "articles": "[articleId, ...]",
     * }
     */
    public void placeOrder(RabbitEvent event) {
        NewPlaceData cart = NewPlaceData.fromJson(event.message.toString());
        try {
            Event data = eventService.placeOrder(cart);
            sendOrderPlaced(data);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    /**
     * @api {topic} order/order-placed Orden Creada
     * @apiGroup RabbitMQ POST
     * @apiDescription Envía de mensajes order-placed desde Order con el topic "order_placed".
     * @apiSuccessExample {json} Mensaje
     * {
     * "type": "order-placed",
     * "message" : {
     * "cartId": "{cartId}",
     * "orderId": "{orderId}"
     * "articles": [{
     * "articleId": "{article id}"
     * "quantity" : {quantity}
     * }, ...]
     * }
     * }
     */
    public void sendOrderPlaced(Event event) {
        RabbitEvent eventToSend = new RabbitEvent();
        eventToSend.type = "order-placed";
        eventToSend.exchange = "order";
        eventToSend.queue = "order";

        eventToSend.message = new OrderPlacedResponse(event.getOrderId(),
                event.placeEvent().getCartId(), event.placeEvent().getArticles());

        topicPublisher.publish("sell_flow", "order_placed", eventToSend);
    }

    private static class OrderPlacedResponse {
        @SerializedName("orderId")
        public String orderId;
        @SerializedName("cartId")
        public String cartId;

        OrderPlacedResponse() {

        }

        OrderPlacedResponse(String orderId, String cartId, PlaceEvent.Article[] articles) {
            this.orderId = orderId;
            this.cartId = cartId;

            Article[] articles1 = Arrays.stream(articles) //
                    .map(a -> new Article(a.getArticleId(), a.getQuantity())) //
                    .collect(Collectors.toList()) //
                    .toArray(new Article[]{});

        }

        public static class Article {

            public Article() {
            }

            public Article(String articleId, int quantity) {
            }
        }
    }
}