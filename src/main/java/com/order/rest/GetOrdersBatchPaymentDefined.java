package com.order.rest;

import com.order.batch.BatchService;
import com.order.rest.tools.Validations;
import io.javalin.Javalin;

import javax.inject.Inject;

/**
 * @api {get} /v1/orders_batch/payment_defined Batch Payment Defined
 * @apiName Batch Payment Defined
 * @apiGroup Ordenes
 * @apiDescription Ejecuta un proceso batch que chequea ordenes en estado PAYMENT_DEFINED.
 * @apiUse AuthHeader
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * @apiUse Errors
 */
public class GetOrdersBatchPaymentDefined {
    final Validations validations;

    final BatchService batchService;

    @Inject
    public GetOrdersBatchPaymentDefined(
            Validations validations,
            BatchService batchService
    ) {
        this.validations = validations;
        this.batchService = batchService;

    }

    public void init(Javalin app) {
        app.get(
                "/v1/orders_batch/payment_defined",
                ctx -> {
                    validations.validateUser(ctx);
                    batchService.processPaymentDefinedOrders();
                    ctx.json("");
                });
    }
}
