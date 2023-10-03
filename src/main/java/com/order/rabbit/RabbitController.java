package com.order.rabbit;

import com.google.gson.annotations.SerializedName;
import com.order.events.schema.Event;
import com.order.events.schema.PlaceEvent;
import com.order.security.TokenService;
import com.order.utils.rabbit.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Singleton
public class RabbitController {
    final TokenService tokenService;

    final FanoutConsumer fanoutConsumer;

    final DirectConsumer directConsumer;

    final TopicPublisher topicPublisher;

    final DirectPublisher directPublisher;

    final EventService eventService;

    public static final Logger logger = Logger.getLogger("rabbit");

    @Inject
    public RabbitController(
            TokenService tokenService,
            FanoutConsumer fanoutConsumer,
            DirectConsumer directConsumer,
            TopicPublisher topicPublisher,
            DirectPublisher directPublisher,
            EventService eventService
    ) {
        this.tokenService = tokenService;
        this.fanoutConsumer = fanoutConsumer;
        this.directConsumer = directConsumer;
        this.topicPublisher = topicPublisher;
        this.directPublisher = directPublisher;
        this.eventService = eventService;
    }

    public void init() {
        fanoutConsumer.init("auth");
        fanoutConsumer.addProcessor("logout", this::logout);
        fanoutConsumer.start();

        directConsumer.init("order", "order");
        directConsumer.addProcessor("place-order", this::placeOrder);
        directConsumer.addProcessor("article-data", this::articleData);
        directConsumer.start();
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
        tokenService.invalidate(event.message.toString());
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

    /**
     * @api {direct} cart/article-data Validación de Artículos
     * @apiGroup RabbitMQ POST
     * @apiDescription Antes de iniciar las operaciones se validan los artículos contra el catalogo.
     * @apiSuccessExample {json} Mensaje
     * {
     * "type": "article-data",
     * "message" : {
     * "cartId": "{cartId}",
     * "articleId": "{articleId}",
     * }
     * }
     */
    public void sendArticleValidation(String orderId, String articleId) {
        ArticleValidationData data = new ArticleValidationData(orderId, articleId);

        RabbitEvent eventToSend = new RabbitEvent();
        eventToSend.type = "article-data";
        eventToSend.exchange = "order";
        eventToSend.queue = "order";
        eventToSend.message = data;

        directPublisher.publish("catalog", "catalog", eventToSend);
    }

    private static class ArticleValidationData {
        ArticleValidationData(String referenceId, String articleId) {
            this.referenceId = referenceId;
            this.articleId = articleId;
        }

        @SerializedName("referenceId")
        public final String referenceId;
        @SerializedName("articleId")
        public final String articleId;
    }
}