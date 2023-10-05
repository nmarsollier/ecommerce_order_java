package com.order.projections.orderStatus;

import com.order.events.EventRepository;
import com.order.events.schema.Event;
import com.order.projections.orderStatus.schema.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStatusService {
    @Autowired
    OrderStatusRepository orderStatusRepository;

    @Autowired
    EventRepository eventRepository;

    // Actualiza la proyección Order
    public void update(Event event) {
        orderStatusRepository
                .findById(event.getOrderId() == null ? event.getId() : event.getOrderId())
                .orElse(new OrderStatus())
                .update(event)
                .storeIn(orderStatusRepository);
    }

    public OrderStatus findById(String orderId) {
        return orderStatusRepository.findById(orderId).orElse(rebuildOrderStatus(orderId));
    }

    // Se elimina y regenera la proyección a partir de los eventos.
    public OrderStatus rebuildOrderStatus(String orderId) {
        List<Event> events = eventRepository.findByOrderId(orderId);
        if (events.isEmpty()) {
            return null;
        }

        orderStatusRepository.deleteById(orderId);
        OrderStatus order = new OrderStatus();
        events.forEach(order::update);
        return order.storeIn(orderStatusRepository);
    }
}