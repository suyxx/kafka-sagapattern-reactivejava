package org.suyash.order.messaging.processor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.common.events.inventory.InventoryEvent;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.common.processor.InventoryEventProcessor;
import org.suyash.order.common.service.OrderFulfillmentService;
import org.suyash.order.common.service.inventory.InventoryComponentStatusListener;
import org.suyash.order.messaging.mapper.InventoryEventMapper;
import org.suyash.order.messaging.mapper.OrderEventMapper;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InventoryEventProcessorImpl implements InventoryEventProcessor<OrderEvent> {

    private final OrderFulfillmentService fulfillmentService;
    private final InventoryComponentStatusListener statusListener;

    @Override
    public Mono<OrderEvent> handle(InventoryEvent.InventoryDeducted event) {
        var dto = InventoryEventMapper.toDto(event);
        return this.statusListener.onSuccess(dto)
                                  .then(this.fulfillmentService.complete(event.orderId()))
                                  .map(OrderEventMapper::toOrderCompletedEvent);
    }

    @Override
    public Mono<OrderEvent> handle(InventoryEvent.InventoryDeclined event) {
        var dto = InventoryEventMapper.toDto(event);
        return this.statusListener.onFailure(dto)
                                  .then(this.fulfillmentService.cancel(event.orderId()))
                                  .map(OrderEventMapper::toOrderCancelledEvent);
    }

    @Override
    public Mono<OrderEvent> handle(InventoryEvent.InventoryRestored event) {
        var dto = InventoryEventMapper.toDto(event);
        return this.statusListener.onRollback(dto)
                                  .then(Mono.empty());
    }

}
