package com.order.projections;

import com.order.events.schema.Event;
import com.order.projections.orderStatus.OrderStatusService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProjectionService {
    final OrderStatusService orderStatusService;

    @Inject
    public ProjectionService(
            OrderStatusService orderStatusService
    ) {
        this.orderStatusService = orderStatusService;
    }

    public void updateProjections(Event event) {
        new Thread(() -> orderStatusService.update(event)).start();
    }
}