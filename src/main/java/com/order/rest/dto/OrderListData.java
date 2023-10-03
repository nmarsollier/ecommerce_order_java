package com.order.rest.dto;

import com.google.gson.annotations.SerializedName;
import com.order.projections.common.Status;
import com.order.projections.orderStatus.schema.OrderStatus;

import java.util.Date;

public class OrderListData {
    @SerializedName("id")
    public final String id;

    @SerializedName("status")
    public final Status status;

    @SerializedName("cartId")
    public final String cartId;

    @SerializedName("totalPrice")
    public final Double totalPrice;

    @SerializedName("totalPayment")
    public final Double totalPayment;
    public final Date updated;
    public final Date created;

    @SerializedName("articles")
    public final int articles;


    public OrderListData(OrderStatus order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.cartId = order.getCartId();
        this.totalPrice = order.getTotalPrice();
        this.totalPayment = order.getPayment();
        this.updated = order.getUpdated();
        this.created = order.getCreated();
        this.articles = order.getArticles();
    }
}