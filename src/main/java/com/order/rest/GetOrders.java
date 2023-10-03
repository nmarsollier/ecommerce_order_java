package com.order.rest;


import com.order.projections.orderStatus.OrderStatusRepository;
import com.order.projections.orderStatus.schema.OrderStatus;
import com.order.rest.dto.OrderListData;
import com.order.rest.tools.Validations;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * @api {get} /v1/orders Ordenes de Usuario
 * @apiName Ordenes de Usuario
 * @apiGroup Ordenes
 * @apiDescription Busca todas las ordenes del usuario logueado.
 * @apiUse AuthHeader
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * [{
 * "id": "{orderID}",
 * "status": "{Status}",
 * "cartId": "{cartId}",
 * "updated": "{updated date}",
 * "created": "{created date}",
 * "totalPrice": {price}
 * "articles": {count}
 * }, ...
 * ]
 * @apiUse Errors
 */
@Singleton
public class GetOrders {
    final OrderStatusRepository repository;

    final Validations validations;

    @Inject
    public GetOrders(
            OrderStatusRepository repository,

            Validations validations
    ) {
        this.repository = repository;
        this.validations = validations;
    }

    public void init(Javalin app) {
        app.get(
                "/v1/orders",
                ctx -> {
                    validations.validateUser(ctx);
                    List<OrderStatus> orders = repository.findByUserId(validations.currentUser(ctx).id);

                    ctx.json(orders.stream().map(OrderListData::new));
                });
    }
}

