package com.order.rabbit;

import com.google.gson.annotations.SerializedName;

import com.order.events.schema.PaymentEvent;
import com.order.utils.gson.Builder;
import com.order.utils.validator.MinLen;
import com.order.utils.validator.MinValue;
import com.order.utils.validator.Required;

public class PaymentData {
    @SerializedName("orderId")
    public String orderId;

    @SerializedName("userId")
    @Required
    @MinLen(1)
    public String userId;

    @SerializedName("method")
    @Required
    public PaymentEvent.Method method;

    @SerializedName("amount")
    @Required
    @MinValue(0)
    public double amount;

    public static PaymentData fromJson(String json) {
        return Builder.gson().fromJson(json, PaymentData.class);
    }
}