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
        OrderStatus order = orderStatusRepository.findById(event.getOrderId()).orElse(null);
        if (order == null) {
            order = new OrderStatus();
        }
        order.update(event);
        orderStatusRepository.save(order);
    }

    public OrderStatus findById(String orderId) {
        OrderStatus order = orderStatusRepository.findById(orderId).orElse(null);
        if (order == null) {
            order = rebuildOrderStatus(orderId);
        }
        return order;
    }

    // Se elimina y regenera la proyección a partir de los eventos.
    public OrderStatus rebuildOrderStatus(String orderId) {
        List<Event> events = eventRepository.findByOrderId(orderId);
        if (events.isEmpty()) {
            return null;
        }

        orderStatusRepository.deleteById(orderId);
        OrderStatus order = new OrderStatus();
        events.forEach(ev -> {
            order.update(ev);
        });
        orderStatusRepository.save(order);
        return order;
    }

}