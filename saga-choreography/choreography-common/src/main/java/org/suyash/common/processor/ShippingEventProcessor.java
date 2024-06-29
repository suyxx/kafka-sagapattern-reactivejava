package org.suyash.common.processor;

import org.suyash.common.events.DomainEvent;
import org.suyash.common.events.shipping.ShippingEvent;
import reactor.core.publisher.Mono;

public interface ShippingEventProcessor<R extends DomainEvent> extends EventProcessor<ShippingEvent, R> {
    @Override
    default Mono<R> process(ShippingEvent event) {
        return switch (event){
            case ShippingEvent.ShippingScheduled e -> this.handle(e);
        };
    }

    Mono<R> handle(ShippingEvent.ShippingScheduled event);
}
