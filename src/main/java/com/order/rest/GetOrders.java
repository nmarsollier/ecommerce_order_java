package com.order.rest;


import com.order.projections.orderStatus.OrderStatusRepository;
import com.order.rest.dto.OrderListData;
import com.order.rest.tools.Validations;
import com.order.utils.errors.SimpleError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
@CrossOrigin
@RestController
public class GetOrders {
    @Autowired
    OrderStatusRepository repository;

    @Autowired
    Validations validations;

    @GetMapping(
            value = "/v1/orders",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<OrderListData> getOrders(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String auth
    ) throws SimpleError {
        validations.validateUser(auth);
        return repository
                .findByUserId(validations.currentUser(auth).id)
                .stream()
                .map(OrderListData::new)
                .toList();
    }
}
