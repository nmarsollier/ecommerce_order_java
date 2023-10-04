package com.order.projections.orderStatus;

import com.order.events.schema.Event;
import com.order.projections.common.Status;
import com.order.projections.orderStatus.schema.OrderStatus;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusRepository extends CrudRepository<OrderStatus, String> {

    @Aggregation(pipeline = {
            "{$match: {'userId': $0}}",
            "{$sort: {created: 1}}"
    })
    List<OrderStatus> findByUserId(String userId);

    @Aggregation(pipeline = {
            "{$match: {'status': $0}}",
            "{$sort: {created: 1}}"
    })
    List<OrderStatus> findByStatus(Status status);
}