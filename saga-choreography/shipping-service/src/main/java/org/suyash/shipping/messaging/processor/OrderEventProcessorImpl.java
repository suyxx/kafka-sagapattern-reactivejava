package org.suyash.shipping.messaging.processor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.common.events.shipping.ShippingEvent;
import org.suyash.common.exception.EventAlreadyProcessedException;
import org.suyash.common.processor.OrderEventProcessor;
import org.suyash.shipping.common.service.ShippingService;
import org.suyash.shipping.messaging.mapper.MessageDtoMapper;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class OrderEventProcessorImpl implements OrderEventProcessor<ShippingEvent> {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProcessorImpl.class);
    private final ShippingService service;


    @Override
    public Mono<ShippingEvent> hadle(OrderEvent.OrderCreated e) {
        return this.service.addShipment(MessageDtoMapper.toScheduleRequest(e))
                .transform(exceptionHandler())
                .then(Mono.empty());
    }

    @Override
    public Mono<ShippingEvent> hadle(OrderEvent.OrderCancelled e) {
       return this.service.cancel(e.orderId())
                .then(Mono.empty());
    }

    @Override
    public Mono<ShippingEvent> hadle(OrderEvent.OrderCompleted e) {
        return this.service.schedule(e.orderId())
                .map(MessageDtoMapper::toShippingScheduledEvent)
                .doOnNext(event -> log.info("shipping scheduled {}", event));
    }

    private <T> UnaryOperator<Mono<T>> exceptionHandler() {
        return mono -> mono.onErrorResume(EventAlreadyProcessedException.class, ex -> Mono.empty())
                .doOnError(ex -> log.error(ex.getMessage()));
    }
}
