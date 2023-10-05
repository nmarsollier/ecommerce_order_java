package com.order.rabbit;

import com.google.gson.annotations.SerializedName;
import com.order.events.schema.Event;
import com.order.events.schema.PlaceEvent;
import com.order.utils.rabbit.RabbitEvent;
import com.order.utils.rabbit.TopicPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class EmitOrderPlaced {
    @Autowired
    TopicPublisher topicPublisher;

    public static final Logger logger = Logger.getLogger("rabbit");


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
            //
            //
            //
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