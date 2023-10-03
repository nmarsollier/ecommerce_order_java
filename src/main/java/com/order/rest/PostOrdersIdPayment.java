package com.order.rest;

import com.order.rabbit.EventService;
import com.order.rabbit.PaymentData;
import com.order.rest.tools.Validations;
import com.order.security.User;
import com.order.utils.gson.Builder;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @api {post} /v1/orders/:orderId/payment Agregar Pago
 * @apiName Agrega un Pago
 * @apiGroup Pagos
 * @apiUse AuthHeader
 * @apiExample {json} Body
 * {
 * "paymentMethod": "CASH | CREDIT | DEBIT",
 * "amount": "{amount}"
 * }
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * @apiUse Errors
 */
@Singleton
public class PostOrdersIdPayment {
    final Validations validations;

    final EventService service;

    @Inject
    public PostOrdersIdPayment(
            Validations validations,
            EventService service
    ) {
        this.validations = validations;
        this.service = service;
    }

    public void init(Javalin app) {
        app.post(
                "/v1/orders/{orderId}/payment",
                ctx -> {
                    validations.validateUser(ctx);
                    validations.validateOrderId(ctx);

                    String orderId = ctx.pathParam("orderId");
                    User user = validations.currentUser(ctx);

                    PaymentData payment = Builder.gson().fromJson(ctx.body(), PaymentData.class);
                    payment.orderId = orderId;
                    payment.userId = user.id;

                    service.placePayment(payment);
                });
    }
}

