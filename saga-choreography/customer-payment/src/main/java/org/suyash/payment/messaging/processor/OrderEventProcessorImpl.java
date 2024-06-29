package org.suyash.payment.messaging.processor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.common.events.payment.PaymentEvent;
import org.suyash.common.exception.EventAlreadyProcessedException;
import org.suyash.common.processor.OrderEventProcessor;
import org.suyash.payment.common.service.PaymentService;
import org.suyash.payment.messaging.mapper.MessageDtoMapper;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;


@Service
@RequiredArgsConstructor
public class OrderEventProcessorImpl implements OrderEventProcessor<PaymentEvent> {

    private final PaymentService service;
    private static final Logger log = LoggerFactory.getLogger(OrderEventProcessorImpl.class);

    @Override
    public Mono<PaymentEvent> hadle(OrderEvent.OrderCreated e) {
        return this.service.process(MessageDtoMapper.toPaymentProcessRequest(e))
                .map(MessageDtoMapper::toPaymentDeductedEvent)
                .doOnNext(event -> log.info("payment processed {}", event))
                .transform(exceptionHandler(e));
    }

    @Override
    public Mono<PaymentEvent> hadle(OrderEvent.OrderCancelled e) {
        return this.service.refund(e.orderId())
                .map(MessageDtoMapper::toPaymentRefundedEvent)
                .doOnNext(event -> log.info("refund processed {}", event))
                .doOnError(ex -> log.error("error while processing refund", ex));

    }

    @Override
    public Mono<PaymentEvent> hadle(OrderEvent.OrderCompleted e) {
        return Mono.empty();
    }

    private UnaryOperator<Mono<PaymentEvent>> exceptionHandler(OrderEvent.OrderCreated event){
        return mono -> mono.onErrorResume(EventAlreadyProcessedException.class, e-> Mono.empty())
                .onErrorResume(MessageDtoMapper.toPaymentDeclinedEvent(event));
    }
}
