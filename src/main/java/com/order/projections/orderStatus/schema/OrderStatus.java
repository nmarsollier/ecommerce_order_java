package com.order.projections.orderStatus.schema;

import com.order.events.schema.Event;
import com.order.projections.common.Status;
import com.order.projections.order.schema.Order;
import com.order.projections.orderStatus.OrderStatusRepository;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Es el Agregado principal de Articulo.
 */
@Document

public final class OrderStatus {
    @Id
    public String id;

    Status status;

    String userId;

    String cartId;
    int articles;

    double payment;

    double totalPrice;

    Date updated = new Date();
    final Date created = new Date();

    public OrderStatus() {
    }

    public OrderStatus update(Event event) {
        OrderStatusUpdater.getUpdaterForEvent(event.getType()).update(this, event);
        return this;
    }

    public OrderStatus update(Order order) {
        status = order.getStatus();
        totalPrice = order.getTotalPrice();
        return this;
    }

    public String getId() {
        return id;
    }

    public String getCartId() {
        return cartId;
    }

    public String getUserId() {
        return userId;
    }

    public Status getStatus() {
        return status;
    }

    public int getArticles() {
        return articles;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getPayment() {
        return payment;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public OrderStatus storeIn(OrderStatusRepository repository) {
        repository.save(this);
        return this;
    }
}
