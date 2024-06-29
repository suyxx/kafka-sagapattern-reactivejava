package org.suyash.common.processor;

import org.springframework.core.annotation.Order;
import org.suyash.common.events.DomainEvent;
import org.suyash.common.events.order.OrderEvent;
import reactor.core.publisher.Mono;

public interface OrderEventProcessor<R extends DomainEvent> extends EventProcessor<OrderEvent, R>{
    @Override
    default Mono<R> process(OrderEvent event) {
        return switch (event){
            case OrderEvent.OrderCreated e -> this.hadle(e);
            case OrderEvent.OrderCancelled e -> this.hadle(e);
            case OrderEvent.OrderCompleted e -> this.hadle(e);
        };

    }

    Mono<R> hadle(OrderEvent.OrderCreated e);
    Mono<R> hadle(OrderEvent.OrderCancelled e);
    Mono<R> hadle(OrderEvent.OrderCompleted e);


}
