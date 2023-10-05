package com.order.rest;

import com.order.events.EventService;
import com.order.rabbit.dto.PaymentData;
import com.order.security.User;
import com.order.utils.errors.SimpleError;
import com.order.utils.errors.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
@CrossOrigin
@RestController
public class PostOrdersIdPayment {
    @Autowired
    Validations validations;

    @Autowired
    EventService service;

    @PostMapping(
            value = "/v1/orders/{orderId}/payment",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void postOrdersIdPayment(
            @PathVariable("orderId") String orderId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String auth,
            @RequestBody PaymentData payment
    ) throws SimpleError, ValidationError {
        validations.validateUser(auth);
        validations.validateOrderId(orderId);

        User user = validations.currentUser(auth);

        payment.orderId = orderId;
        payment.userId = user.id;

        service.placePayment(payment);
    }
}

