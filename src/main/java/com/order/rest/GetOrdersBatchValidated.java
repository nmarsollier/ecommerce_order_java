package com.order.rest;

import com.order.batch.BatchService;
import com.order.rest.tools.Validations;
import io.javalin.Javalin;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @api {get} /v1/orders_batch/validated Batch Validated
 * @apiName Batch Validated
 * @apiGroup Ordenes
 * @apiDescription Ejecuta un proceso batch para ordenes en estado VALIDATED.
 * @apiUse AuthHeader
 * @apiSuccessExample {json} Respuesta
 * HTTP/1.1 200 OK
 * @apiUse Errors
 */
@Singleton
public class GetOrdersBatchValidated {
    final Validations validations;

    final BatchService batchService;

    @Inject
    public GetOrdersBatchValidated(
            Validations validations,
            BatchService batchService
    ) {
        this.validations = validations;
        this.batchService = batchService;
    }

    public void init(Javalin app) {
        app.get(
                "/v1/orders_batch/validated",
                ctx -> {
                    validations.validateUser(ctx);
                    batchService.processValidatedOrders();
                    ctx.json("");
                });
    }
}
