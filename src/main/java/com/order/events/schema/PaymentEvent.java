package com.order.events.schema;

public class PaymentEvent {
    private String userId;
    private Method method;
    private double amount;
    
    public PaymentEvent(String userId, Method method, double amount) {
        this.userId = userId;
        this.method = method;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public Method getMethod() {
        return method;
    }

    public enum Method {
        CASH, CREDIT, DEBIT
    }
}
