package com.order.rabbit;

import com.google.gson.annotations.SerializedName;
import com.order.utils.rabbit.DirectPublisher;
import com.order.utils.rabbit.RabbitEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class EmitArticleValidation {
    @Autowired
    DirectPublisher directPublisher;

    public static final Logger logger = Logger.getLogger("rabbit");

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