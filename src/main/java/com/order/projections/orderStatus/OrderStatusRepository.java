package com.order.projections.orderStatus;

import com.google.common.collect.Streams;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.order.projections.common.Status;
import com.order.projections.orderStatus.schema.OrderStatus;
import com.order.utils.db.MongoStore;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class OrderStatusRepository {
    final MongoStore mongoStore;

    private final MongoCollection<OrderStatus> collection;

    @Inject
    public OrderStatusRepository(MongoStore mongoStore) {
        this.mongoStore = mongoStore;
        collection = mongoStore.getOrderStatusCollection();
    }

    /**
     * Devuelve una orden especifica
     */
    public OrderStatus findById(String id) {
        return collection.find(Filters.eq("_id", new ObjectId(id))).first();
    }

    /**
     * Devuelve las ordenes de un usuario especifico
     */
    public List<OrderStatus> findByUserId(String userId) {
        return Streams.stream(collection
                .find(
                        Filters.eq("userId", userId)
                )
                .sort(Sorts.ascending("created"))
                .iterator()
        ).toList();
    }

    /**
     * Devuelve las ordenes que tienen un estado en particular
     */
    public List<OrderStatus> findByStatus(Status status) {
        return Streams.stream(collection
                .find(
                        Filters.eq("status", status)
                )
                .sort(Sorts.ascending("created"))
                .iterator()
        ).toList();
    }


    public void save(OrderStatus order) {
        collection.insertOne(order);
    }

    public void delete(String orderId) {
        collection.deleteOne(Filters.eq("_id", new ObjectId(orderId)));
    }
}