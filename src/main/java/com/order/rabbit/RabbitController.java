package com.order.rabbit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitController {
    @Autowired
    ConsumeArticleData consumeArticleData;

    @Autowired
    ConsumeAuthLogout consumeAuthLogout;

    @Autowired
    ConsumerPlaceOrder consumerPlaceOrder;

    public void init() {
        consumeArticleData.init();
        consumeAuthLogout.init();
        consumerPlaceOrder.init();
    }
}