package com.order.events;

import com.order.events.schema.Event;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends CrudRepository<Event, String> {

    @Query("{ $and [{'placeEvent.cartId': ?0}, {'type': 'PLACE_ORDER'}] }")
    List<Event> findPlaceByCartId(String cartId);

    @Query("{ $and [{'orderId': ?0}, {'type': 'PLACE_ORDER'}] }")
    List<Event> findPlaceByOrderId(String orderId);

    @Aggregation(pipeline = {
            "{$match: { $or [{'orderId': $0}, {'_id': $0}] }",
            "{$sort: {created: 1}}"
    })
    List<Event> findByOrderId(String orderId);
}