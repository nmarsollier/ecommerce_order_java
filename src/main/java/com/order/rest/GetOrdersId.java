package com.order.rest;

import com.order.projections.order.OrderService;
import com.order.projections.order.schema.Order;
import com.order.rest.tools.Validations;
import com.order.utils.errors.UnauthorizedError;
import com.order.utils.errors.ValidationError;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @api {get} /v1/orders/:orderId Buscar Orden
 * @apiName Buscar Orden
 * @apiGroup Ordenes
 * @apiDescription Busca una order del usuario logueado, por su id.
 * @apiUse AuthHeader
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * {
 * "id": "{orderID}",
 * "status": "{Status}",
 * "cartId": "{cartId}",
 * "updated": "{updated date}",
 * "created": "{created date}",
 * "articles": [
 * {
 * "id": "{articleId}",
 * "quantity": {quantity},
 * "validated": true|false,
 * "valid": true|false
 * }, ...
 * ]
 * }
 * @apiUse Errors
 */
@Singleton
public class GetOrdersId {
    final Validations validations;

    final OrderService service;

    @Inject
    public GetOrdersId(
            Validations validations,

            OrderService service

    ) {
        this.validations = validations;
        this.service = service;
    }


    public void init(Javalin app) {
        app.get(
                "/v1/orders/{orderId}",
                ctx -> {
                    validations.validateUser(ctx);
                    validations.validateOrderId(ctx);
                    Order order = service.buildOrder(ctx.queryParam("orderId"));
                    if (order == null) throw new ValidationError().addPath("orderId", "Not Found");

                    if (!order.getUserId().equals(validations.currentUser(ctx).id)) {
                        throw new UnauthorizedError();
                    }

                    ctx.json(order);
                }
        );
    }
}
