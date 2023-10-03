package com.order.projections.order;

import com.order.events.EventRepository;
import com.order.events.schema.Event;
import com.order.projections.order.schema.Order;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class OrderService {
    final EventRepository eventRepository;

    @Inject
    public OrderService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Se elimina y regenera la proyección a partir de los eventos.
    public Order buildOrder(String orderId) {
        List<Event> events = eventRepository.findByOrderId(orderId);
        if (events.isEmpty()) {
            return null;
        }

        Order order = new Order();
        events.forEach(order::update);
        return order;
    }

}