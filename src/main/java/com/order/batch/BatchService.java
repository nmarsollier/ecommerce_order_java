package com.order.batch;

import com.order.events.EventRepository;
import com.order.events.schema.Event;
import com.order.events.schema.EventType;
import com.order.projections.common.Status;
import com.order.projections.order.OrderService;
import com.order.projections.order.schema.Order;
import com.order.projections.orderStatus.OrderStatusRepository;
import com.order.rabbit.RabbitController;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class BatchService {
    final OrderService orderService;

    final OrderStatusRepository statusRepository;

    final EventRepository eventRepository;

    final RabbitController rabbitController;

    @Inject
    public BatchService(
            OrderService orderService,
            OrderStatusRepository statusRepository,
            EventRepository eventRepository,
            RabbitController rabbitController
    ) {
        this.orderService = orderService;
        this.statusRepository = statusRepository;
        this.eventRepository = eventRepository;
        this.rabbitController = rabbitController;
    }

    private final AtomicBoolean placeOrdersRunning = new AtomicBoolean();
    private final AtomicBoolean validatedOrdersRunning = new AtomicBoolean();

    public void processPlacedOrders() {
        new Thread(() -> {
            if (!placeOrdersRunning.compareAndSet(false, true)) {
                return;
            }

            statusRepository
                    .findByStatus(Status.PLACED)
                    .forEach(stat -> {
                        Order order = orderService.buildOrder(stat.getId());

                        // Si status de order sigue en PLACED es porque no se validó completamente
                        if (order.getStatus() == Status.PLACED) {
                            validateOrder(order.getId());
                        } else {
                            // En este caso OrderStatus esta desactualizado
                            stat.update(order).storeIn(statusRepository);
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

            statusRepository.findByStatus(Status.VALIDATED)
                    .forEach(stat -> {
                        stat.update(orderService.buildOrder(stat.getId()))
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

            statusRepository.findByStatus(Status.PAYMENT_DEFINED).forEach(stat -> {
                Order order = orderService.buildOrder(stat.getId());
                stat.update(order).storeIn(statusRepository);

                if (order.getStatus() == Status.PAYMENT_DEFINED) {
                    // A reservar artículos

                }
            });

            validatedOrdersRunning.set(false);
        }).start();
    }

    private void validateOrder(String orderId) {
        Event event = eventRepository.findPlaceByOrderId(orderId);
        if (event != null) {
            /**
             * Busca todos los artículos de un evento, los envía a rabbit para que catalog valide si están activos
             */
            new Thread(() -> {
                if (event.getType() == EventType.PLACE_ORDER) {
                    Arrays.stream(event.placeEvent().getArticles()) //
                            .forEach(a -> {
                                rabbitController.sendArticleValidation(event.getOrderId(), a.getArticleId());
                            });
                }
            }).start();
        }
    }
}