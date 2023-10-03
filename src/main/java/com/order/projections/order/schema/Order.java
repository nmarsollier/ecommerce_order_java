package com.order.projections.order.schema;

import com.order.events.schema.Event;
import com.order.events.schema.PaymentEvent;
import com.order.projections.common.Status;
import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import java.util.Arrays;
import java.util.Date;

/**
 * Es el Agregado principal de Articulo.
 */

public final class Order {
    @BsonId
    @BsonRepresentation(BsonType.OBJECT_ID)
    String id;

    Status status;

    String userId;

    String cartId;
    Article[] articles = new Article[]{};

    Payment[] payment = new Payment[]{};

    Date updated = new Date();
    final Date created = new Date();

    public Order() {
    }

    public void update(Event event) {
        OrderUpdater.getUpdaterForEvent(event.getType()).update(this, event);
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

    public Article[] getArticles() {
        return articles;
    }

    public Payment[] getPayment() {
        return payment;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public double getTotalPrice() {
        return Arrays.stream(articles).mapToDouble(Article::getTotalPrice).sum();
    }

    public double getTotalPayment() {
        return Arrays.stream(payment).mapToDouble(a -> a.amount).sum();
    }


    public static class Article {
        final String id;
        final int quantity;
        double unitaryPrice;
        boolean valid;
        boolean validated;

        protected Article(String id, int quantity) {
            this.id = id;
            this.quantity = quantity;
        }

        public String getId() {
            return id;
        }

        public int getQuantity() {
            return quantity;
        }

        public boolean isValid() {
            return valid;
        }

        public boolean isValidated() {
            return validated;
        }

        public double getUnitaryPrice() {
            return unitaryPrice;
        }

        public double getTotalPrice() {
            return unitaryPrice * quantity;
        }
    }

    public static class Payment {
        final PaymentEvent.Method method;
        final double amount;

        protected Payment(PaymentEvent.Method method, double amount) {
            this.method = method;
            this.amount = amount;
        }

        public double getAmount() {
            return amount;
        }

        public PaymentEvent.Method getMethod() {
            return method;
        }
    }
}
