package org.suyash.inventory.messaging.processor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.suyash.common.events.inventory.InventoryEvent;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.common.exception.EventAlreadyProcessedException;
import org.suyash.common.processor.OrderEventProcessor;
import org.suyash.inventory.common.service.InventoryService;
import org.suyash.inventory.messaging.mapper.MessageDtoMapper;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;


@Service
@RequiredArgsConstructor
public class OrderEventProcessorImpl implements OrderEventProcessor<InventoryEvent> {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProcessorImpl.class);
    private final InventoryService service;


    @Override
    public Mono<InventoryEvent> hadle(OrderEvent.OrderCreated e) {
        return this.service.deduct(MessageDtoMapper.toInventoryDeductRequest(e))
                .map(MessageDtoMapper::toInventoryDeductEvent)
                .doOnNext(event -> log.info("inventory deducted {}", event))
                .transform(exceptionHandler(e));
    }

    private UnaryOperator<Mono<InventoryEvent>> exceptionHandler(OrderEvent.OrderCreated event){
        return mono -> mono.onErrorResume(EventAlreadyProcessedException.class, e-> Mono.empty())
                .onErrorResume(MessageDtoMapper.toInventoryDeclinedEvent(event));
    }

    @Override
    public Mono<InventoryEvent> hadle(OrderEvent.OrderCancelled e) {
        return this.service.restore(e.orderId())
                .map(MessageDtoMapper::toInventoryRestoredEvent)
                .doOnNext(event -> log.info("inventory restored {}", event))
                .doOnNext(ex -> log.error("error while processing restore {}", ex));
    }

    @Override
    public Mono<InventoryEvent> hadle(OrderEvent.OrderCompleted e) {
        return
                Mono.empty();
    }
}
