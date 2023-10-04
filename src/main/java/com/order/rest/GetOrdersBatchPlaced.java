package com.order.rest;

import com.order.batch.BatchService;
import com.order.rest.tools.Validations;
import com.order.utils.errors.SimpleError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
@CrossOrigin
@RestController
public class GetOrdersBatchPlaced {
    @Autowired
    Validations validations;

    @Autowired
    BatchService batchService;

    @GetMapping(
            value = "/v1/orders_batch/placed",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void getOrdersBatchPlaced(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String auth
    ) throws SimpleError {
        validations.validateUser(auth);
        batchService.processPlacedOrders();
    }
}
