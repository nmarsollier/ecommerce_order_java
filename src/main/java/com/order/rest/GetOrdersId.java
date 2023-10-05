package com.order.rest;

import com.order.projections.order.OrderService;
import com.order.projections.order.schema.Order;
import com.order.utils.errors.SimpleError;
import com.order.utils.errors.UnauthorizedError;
import com.order.utils.errors.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
@CrossOrigin
@RestController
public class GetOrdersId {
    @Autowired
    Validations validations;

    @Autowired
    OrderService service;

    @GetMapping(
            value = "/v1/orders/{orderId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Order getOrdersId(
            @PathVariable("orderId") String orderId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String auth
    ) throws SimpleError, ValidationError {
        validations.validateUser(auth);
        validations.validateOrderId(orderId);
        Order order = service.buildOrder(orderId);
        if (order == null) throw new ValidationError().addPath("orderId", "Not Found");

        if (!order.getUserId().equals(validations.currentUser(auth).id)) {
            throw new UnauthorizedError();
        }

        return order;
    }
}
