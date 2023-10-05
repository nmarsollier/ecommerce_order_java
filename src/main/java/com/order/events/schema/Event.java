package com.order.events.schema;

import com.order.events.EventRepository;
import com.order.projections.ProjectionService;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Permite almacenar los eventos del event store.
 */
@Document
public class Event {
    @Id
    private String id;

    private String orderId;

    private EventType type;

    private PlaceEvent placeEvent;

    private ArticleValidationEvent articleValidationEvent;

    private PaymentEvent payment;

    private Date created = new Date();

    private Event() {
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public EventType getType() {
        return type;
    }

    public Date getCreated() {
        return created;
    }

    public ArticleValidationEvent articleValidationEvent() {
        return articleValidationEvent;
    }

    public PlaceEvent placeEvent() {
        return placeEvent;
    }

    public PaymentEvent paymentEvent() {
        return payment;
    }

    // Crea un nuevo evento de place order
    public static Event newPlaceOrder(PlaceEvent placeEvent) {
        Event result = new Event();
        result.type = EventType.PLACE_ORDER;
        result.placeEvent = placeEvent;
        return result;
    }

    public static Event newArticleValidation(String orderId, ArticleValidationEvent validationEvent) {
        Event result = new Event();
        result.orderId = orderId;
        result.type = EventType.ARTICLE_VALIDATION;
        result.articleValidationEvent = validationEvent;
        return result;
    }

    public static Event newPayment(String orderId, PaymentEvent payment) {
        Event result = new Event();
        result.orderId = orderId;
        result.type = EventType.PAYMENT;
        result.payment = payment;
        return result;
    }

    public Event saveIn(EventRepository repository) {
        repository.save(this);
        return this;
    }

    public Event updateProjection(ProjectionService projectionService) {
        projectionService.updateProjections(this);
        return this;
    }
}
