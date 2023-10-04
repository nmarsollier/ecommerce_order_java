package com.order.projections;

import com.order.events.schema.Event;
import com.order.projections.orderStatus.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectionService {
    @Autowired
    OrderStatusService orderStatusService;

    public void updateProjections(Event event) {
        new Thread(() -> orderStatusService.update(event)).start();
    }
}