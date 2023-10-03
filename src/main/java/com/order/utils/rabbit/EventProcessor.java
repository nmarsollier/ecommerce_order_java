package com.order.utils.rabbit;

@FunctionalInterface
public interface EventProcessor {
    void process(RabbitEvent event);
}