package com.order.projections.order.schema;

import com.order.events.schema.Event;
import com.order.events.schema.EventType;
import com.order.projections.common.Status;

import java.util.Arrays;
import java.util.Date;

public interface OrderUpdater {
    void update(Order order, Event event);

    static OrderUpdater getUpdaterForEvent(EventType type) {
        return switch (type) {
            case PLACE_ORDER -> new PlaceEventUpdater();
            case ARTICLE_VALIDATION -> new ArticleValidationUpdater();
            case PAYMENT -> new PaymentUpdater();
        };
    }

    class PlaceEventUpdater implements OrderUpdater {
        @Override
        public void update(Order order, Event event) {
            order.id = event.getOrderId();
            order.userId = event.placeEvent().getUserId();
            order.cartId = event.placeEvent().getCartId();
            order.status = Status.PLACED;

            order.articles = Arrays.stream(event.placeEvent().getArticles()) //
                    .map(article -> new Order.Article(article.getArticleId(), article.getQuantity())) //
                    .toList() //
                    .toArray(new Order.Article[]{});

            order.updated = new Date();
        }
    }

    class ArticleValidationUpdater implements OrderUpdater {
        @Override
        public void update(Order order, Event event) {
            Arrays.stream(order.articles) //
                    .filter(a -> a.id.equals(event.articleValidationEvent().getArticleId())) //
                    .forEach(a -> {
                        a.valid = event.articleValidationEvent().isValid();
                        a.unitaryPrice = event.articleValidationEvent().getPrice();
                        a.validated = true;
                    });

            if (!event.articleValidationEvent().isValid()) {
                order.status = Status.INVALID;
            }

            if (Arrays.stream(order.articles).filter(Order.Article::isValid).count() == order.articles.length) {
                // Todos validos cambiamos el estado de la orden
                order.status = Status.VALIDATED;
            }

            order.updated = new Date();
        }
    }

    class PaymentUpdater implements OrderUpdater {
        @Override
        public void update(Order order, Event event) {
            Order.Payment[] payments = new Order.Payment[order.payment.length + 1];

            System.arraycopy(order.payment, 0, payments, 0, order.payment.length);
            payments[payments.length - 1] = new Order.Payment(event.paymentEvent().getMethod(),
                    event.paymentEvent().getAmount());

            order.payment = payments;

            if (order.getTotalPayment() >= order.getTotalPrice()) {
                order.status = Status.PAYMENT_DEFINED;
            }
        }
    }
}
