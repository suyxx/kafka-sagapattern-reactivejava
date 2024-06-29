package org.suyash.order.messaging.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.common.events.payment.PaymentEvent;
import org.suyash.common.processor.PaymentEventProcessor;
import org.suyash.order.common.service.OrderFulfillmentService;
import org.suyash.order.common.service.payment.PaymentComponentStatusListener;
import org.suyash.order.messaging.mapper.OrderEventMapper;
import org.suyash.order.messaging.mapper.PaymentEventMapper;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentEventProcessorImpl implements PaymentEventProcessor<OrderEvent> {

    private final OrderFulfillmentService fulfillmentService;
    private final PaymentComponentStatusListener statusListener;

    @Override
    public Mono<OrderEvent> handle(PaymentEvent.PaymentDeducted event) {
        var dto = PaymentEventMapper.toDto(event);
        return this.statusListener.onSuccess(dto)
                                  .then(this.fulfillmentService.complete(event.orderId()))
                                  .map(OrderEventMapper::toOrderCompletedEvent);
    }

    @Override
    public Mono<OrderEvent> handle(PaymentEvent.PaymentDeclined event) {
        var dto = PaymentEventMapper.toDto(event);
        return this.statusListener.onFailure(dto)
                                  .then(this.fulfillmentService.cancel(event.orderId()))
                                  .map(OrderEventMapper::toOrderCancelledEvent);
    }

    @Override
    public Mono<OrderEvent> handle(PaymentEvent.PaymentRefunded event) {
        var dto = PaymentEventMapper.toDto(event);
        return this.statusListener.onRollback(dto)
                                  .then(Mono.empty());
    }
}
