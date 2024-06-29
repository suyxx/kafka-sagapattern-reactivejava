package org.suyash.common.processor;

import org.suyash.common.events.DomainEvent;
import org.suyash.common.events.inventory.InventoryEvent;
import org.suyash.common.events.order.OrderEvent;
import reactor.core.publisher.Mono;

public interface InventoryEventProcessor<R extends DomainEvent> extends EventProcessor<InventoryEvent, R> {
    @Override
    default Mono<R> process(InventoryEvent event) {
        return switch (event){
            case InventoryEvent.InventoryDeclined e-> this.handle(e);
            case InventoryEvent.InventoryDeducted e-> this.handle(e);
            case InventoryEvent.InventoryRestored e-> this.handle(e);

        };
    }

    Mono<R> handle(InventoryEvent.InventoryDeducted event);
    Mono<R> handle(InventoryEvent.InventoryRestored event);
    Mono<R> handle(InventoryEvent.InventoryDeclined event);
}
