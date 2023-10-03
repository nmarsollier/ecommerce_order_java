package com.order.projections.orderStatus;

import com.order.events.EventRepository;
import com.order.events.schema.Event;
import com.order.projections.orderStatus.schema.OrderStatus;

import javax.inject.Inject;
import java.util.List;

public class OrderStatusService {
    final OrderStatusRepository orderStatusRepository;

    final EventRepository eventRepository;

    @Inject
    public OrderStatusService(
            OrderStatusRepository orderStatusRepository,
            EventRepository eventRepository
    ) {
        this.orderStatusRepository = orderStatusRepository;
        this.eventRepository = eventRepository;
    }

    // Actualiza la proyección Order
    public void update(Event event) {
        OrderStatus order = orderStatusRepository.findById(event.getOrderId());
        if (order == null) {
            order = new OrderStatus();
        }
        order.update(event);
        orderStatusRepository.save(order);
    }

    public OrderStatus findById(String orderId) {
        OrderStatus order = orderStatusRepository.findById(orderId);
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

        orderStatusRepository.delete(orderId);
        OrderStatus order = new OrderStatus();
        events.forEach(ev -> {
            order.update(ev);
        });
        orderStatusRepository.save(order);
        return order;
    }

}