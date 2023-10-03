package com.order.projections.orderStatus.schema;

import com.order.events.schema.Event;
import com.order.events.schema.EventType;
import com.order.projections.common.Status;

import java.util.Date;

public interface OrderStatusUpdater {
    void update(OrderStatus order, Event event);

    static OrderStatusUpdater getUpdaterForEvent(EventType type) {
        switch (type) {
            case PLACE_ORDER:
                return new PlaceEventUpdater();
            case ARTICLE_VALIDATION:
                return new ArticleValidationUpdater();
            case PAYMENT:
                return new PaymentUpdater();
        }
        return new VoidEventUpdater();
    }

    class PlaceEventUpdater implements OrderStatusUpdater {
        @Override
        public void update(OrderStatus order, Event event) {
            order.id = event.getOrderId();
            order.userId = event.placeEvent().getUserId();
            order.cartId = event.placeEvent().getCartId();
            order.status = Status.PLACED;
            order.articles = event.placeEvent().getArticles().length;
            order.updated = new Date();
        }
    }

    class ArticleValidationUpdater implements OrderStatusUpdater {
        @Override
        public void update(OrderStatus order, Event event) {
            if (!event.articleValidationEvent().isValid()) {
                order.status = Status.INVALID;
            }

            order.updated = new Date();
        }
    }

    class PaymentUpdater implements OrderStatusUpdater {
        @Override
        public void update(OrderStatus order, Event event) {
            order.payment += event.paymentEvent().getAmount();
        }
    }

    class VoidEventUpdater implements OrderStatusUpdater {
        @Override
        public void update(OrderStatus order, Event event) {

        }
    }

}
