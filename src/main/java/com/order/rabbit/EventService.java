package com.order.rabbit;

import com.order.events.EventRepository;
import com.order.events.schema.ArticleValidationEvent;
import com.order.events.schema.Event;
import com.order.events.schema.PlaceEvent;
import com.order.projections.ProjectionService;
import com.order.utils.errors.ValidationError;
import com.order.utils.validator.Validator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.stream.Collectors;

@Singleton
public class EventService {
    final Validator validator;

    final EventRepository eventRepository;

    final ProjectionService projectionService;

    @Inject
    public EventService(
            Validator validator,
            EventRepository eventRepository,
            ProjectionService projectionService
    ) {
        this.validator = validator;
        this.eventRepository = eventRepository;
        this.projectionService = projectionService;
    }

    public Event placeOrder(NewPlaceData data) throws ValidationError {
        validator.validate(data);

        Event event = eventRepository.findPlaceByCartId(data.cartId);
        if (event == null) {
            PlaceEvent.Article[] articles = Arrays.stream(data.articles) //
                    .map(a -> new PlaceEvent.Article(a.id, a.quantity)) //
                    .collect(Collectors.toList()) //
                    .toArray(new PlaceEvent.Article[]{});

            PlaceEvent place = new PlaceEvent(data.cartId, data.userId, articles);

            event = Event.newPlaceOrder(place);
            eventRepository.save(event);
            projectionService.updateProjections(event);
        }

        return event;
    }

    public Event placeArticleExist(NewArticleValidationData data) throws ValidationError {
        validator.validate(data);

        Event event = Event.newArticleValidation(data.orderId,
                new ArticleValidationEvent(data.articleId, data.valid, data.stock, data.price));
        eventRepository.save(event);

        projectionService.updateProjections(event);
        return event;
    }

    public Event placePayment(PaymentData payment) throws ValidationError {
        validator.validate(payment);

        Event event = Event.newPayment(payment.orderId, payment.userId, payment.method, payment.amount);
        eventRepository.save(event);

        projectionService.updateProjections(event);
        return event;
    }
}