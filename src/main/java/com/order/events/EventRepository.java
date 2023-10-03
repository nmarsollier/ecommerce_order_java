package com.order.events;

import com.google.common.collect.Streams;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.order.events.schema.Event;
import com.order.events.schema.EventType;
import com.order.utils.db.MongoStore;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class EventRepository {
    final MongoStore mongoStore;

    private MongoCollection<Event> collection;

    @Inject
    public EventRepository(MongoStore mongoStore) {
        this.mongoStore = mongoStore;
        collection = mongoStore.getEventCollection();
    }

    public void save(Event event) {
        collection.insertOne(event).getInsertedId();
    }

    public Event findPlaceByCartId(String cartId) {
        return collection.find(
                Filters.and(
                        Filters.eq("placeEvent.cartId", cartId),
                        Filters.eq("type", EventType.PLACE_ORDER)
                )
        ).first();
    }


    public Event findPlaceByOrderId(String orderId) {
        return collection.find(
                Filters.and(
                        Filters.eq("orderId", orderId),
                        Filters.eq("type", EventType.PLACE_ORDER)
                )
        ).first();
    }

    public List<Event> findByOrderId(String orderId) {
        return Streams.stream(collection
                .find(
                        Filters.eq("orderId", orderId)
                )
                .sort(Sorts.ascending("created"))
                .iterator()
        ).toList();
    }
}