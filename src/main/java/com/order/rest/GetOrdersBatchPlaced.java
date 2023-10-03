package com.order.rest;

import com.order.batch.BatchService;
import com.order.rest.tools.Validations;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @api {get} /v1/orders_batch/placed Batch Placed
 * @apiName Batch Placed
 * @apiGroup Ordenes
 * @apiDescription Ejecuta un proceso batch que chequea ordenes en estado PLACED.
 * @apiUse AuthHeader
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * @apiUse Errors
 */
@Singleton
public class GetOrdersBatchPlaced {
    final Validations validations;

    final BatchService batchService;

    @Inject
    public GetOrdersBatchPlaced(
            Validations validations,
            BatchService batchService
    ) {
        this.validations = validations;
        this.batchService = batchService;
    }

    public void init(Javalin app) {
        app.get(
                "/v1/orders_batch/placed",
                ctx -> {
                    validations.validateUser(ctx);
                    batchService.processPlacedOrders();
                    ctx.json("");
                });
    }
}
