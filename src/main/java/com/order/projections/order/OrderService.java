package com.order.projections.order;

import com.order.events.EventRepository;
import com.order.events.schema.Event;
import com.order.projections.order.schema.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    EventRepository eventRepository;

    // Se elimina y regenera la proyección a partir de los eventos.
    public Order buildOrder(String orderId) {
        List<Event> events = eventRepository.findByOrderId(orderId);
        if (events.isEmpty()) return null;

        Order order = new Order();
        events.forEach(order::update);
        return order;
    }
}