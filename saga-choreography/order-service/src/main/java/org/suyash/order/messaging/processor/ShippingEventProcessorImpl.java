package org.suyash.order.messaging.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.common.events.shipping.ShippingEvent;
import org.suyash.common.processor.ShippingEventProcessor;
import org.suyash.order.common.service.shipping.ShippingComponentStatusListener;
import org.suyash.order.messaging.mapper.ShippingEventMapper;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ShippingEventProcessorImpl implements ShippingEventProcessor<OrderEvent> {

    private final ShippingComponentStatusListener statusListener;

    @Override
    public Mono<OrderEvent> handle(ShippingEvent.ShippingScheduled event) {
        var dto = ShippingEventMapper.toDto(event);
        return this.statusListener.onSuccess(dto)
                .then(Mono.empty());
    }

}
