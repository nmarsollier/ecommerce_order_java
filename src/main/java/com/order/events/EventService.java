package com.order.events;

import com.order.events.schema.Event;
import com.order.projections.ProjectionService;
import com.order.rabbit.dto.NewArticleValidationData;
import com.order.rabbit.dto.NewPlaceData;
import com.order.rabbit.dto.PaymentData;
import com.order.utils.errors.ValidationError;
import com.order.utils.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    @Autowired
    Validator validator;

    @Autowired
    EventRepository eventRepository;
    @Autowired
    ProjectionService projectionService;

    public Event placeOrder(NewPlaceData data) throws ValidationError {
        validator.validate(data);

        Event event = eventRepository.findPlaceByCartId(data.cartId).stream().findFirst().orElse(null);
        if (event == null) {
            Event.newPlaceOrder(data.toPlaceEvent())
                    .saveIn(eventRepository)
                    .updateProjection(projectionService);
        } else {
            event.updateProjection(projectionService);
        }

        return event;
    }

    public Event placeArticleExist(NewArticleValidationData data) throws ValidationError {
        validator.validate(data);

        return Event.newArticleValidation(data.orderId, data.toArticleValidationEvent())
                .saveIn(eventRepository)
                .updateProjection(projectionService);
    }

    public Event placePayment(PaymentData payment) throws ValidationError {
        validator.validate(payment);

        return Event.newPayment(payment.orderId, payment.toPaymentEvent())
                .saveIn(eventRepository)
                .updateProjection(projectionService);
    }
}