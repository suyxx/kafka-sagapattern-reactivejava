package org.suyash.order.messaging.publisher;

import lombok.RequiredArgsConstructor;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.common.publisher.EventPublisher;
import org.suyash.order.common.dto.PurchaseOrderDto;
import org.suyash.order.common.service.OrderEventListener;
import org.suyash.order.messaging.mapper.OrderEventMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;


@RequiredArgsConstructor
public class OrderEventListenerImpl implements OrderEventListener, EventPublisher<OrderEvent> {

    private final Sinks.Many<OrderEvent> sink;
    private final Flux<OrderEvent> flux;

    @Override
    public Flux<OrderEvent> publish() {
        return this.flux;
    }

    @Override
    public void emitOrderCreated(PurchaseOrderDto dto) {
        var event = OrderEventMapper.toOrderCreatedEvent(dto);
        this.sink.emitNext(
                event,
                Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1))
        );
    }

}
