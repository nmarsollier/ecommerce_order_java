package com.order.batch;

import com.order.events.EventRepository;
import com.order.events.schema.Event;
import com.order.projections.common.Status;
import com.order.projections.order.OrderService;
import com.order.projections.order.schema.Order;
import com.order.projections.orderStatus.OrderStatusRepository;
import com.order.rabbit.EmitArticleValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class BatchService {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderStatusRepository statusRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EmitArticleValidation rabbitController;


    private final AtomicBoolean placeOrdersRunning = new AtomicBoolean();
    private final AtomicBoolean validatedOrdersRunning = new AtomicBoolean();

    public void processPlacedOrders() {
        new Thread(() -> {
            if (!placeOrdersRunning.compareAndSet(false, true)) {
                return;
            }

            statusRepository
                    .findByStatus(Status.PLACED, Sort.by(Sort.Direction.ASC, "created"))
                    .forEach(stat -> {
                        Order order = orderService.buildOrder(stat.getId());

                        // Si status de order sigue en PLACED es porque no se validó completamente
                        if (order.getStatus() == Status.PLACED) {
                            validateOrder(order.getId());
                        } else {
                            // En este caso OrderStatus esta desactualizado
                            stat.update(order.getStatus(), order.getTotalPrice())
                                    .storeIn(statusRepository);
                        }
                    });

            placeOrdersRunning.set(false);
        }).start();
    }

    public void processValidatedOrders() {
        new Thread(() -> {
            if (!validatedOrdersRunning.compareAndSet(false, true)) {
                return;
            }

            statusRepository.findByStatus(Status.VALIDATED, Sort.by(Sort.Direction.ASC, "created"))
                    .forEach(stat -> {
                        Order order = orderService.buildOrder(stat.getId());
                        stat.update(order.getStatus(), order.getTotalPrice())
                                .storeIn(statusRepository);
                    });

            validatedOrdersRunning.set(false);
        }).start();
    }

    public void processPaymentDefinedOrders() {
        new Thread(() -> {
            if (!validatedOrdersRunning.compareAndSet(false, true)) {
                return;
            }

            statusRepository.findByStatus(Status.PAYMENT_DEFINED, Sort.by(Sort.Direction.ASC, "created")).forEach(stat -> {
                Order order = orderService.buildOrder(stat.getId());
                stat.update(order.getStatus(), order.getTotalPrice()).storeIn(statusRepository);
            });

            validatedOrdersRunning.set(false);
        }).start();
    }

    private void validateOrder(String orderId) {
        Event event = eventRepository.findPlaceByOrderId(orderId).stream().findFirst().orElse(null);
        if (event == null) {
            return;
        }

        /**
         * Busca todos los artículos de un evento, los envía a rabbit para que catalog valide si están activos
         */
        new Thread(() -> {
            Arrays.stream(event.placeEvent().getArticles()) //
                    .forEach(a -> {
                        rabbitController.sendArticleValidation(
                                event.getOrderId() == null ? event.getId() : event.getOrderId(),
                                a.getArticleId()
                        );
                    });
        }).start();
    }
}