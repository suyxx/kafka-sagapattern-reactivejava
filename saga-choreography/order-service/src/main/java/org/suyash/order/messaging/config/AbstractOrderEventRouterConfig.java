package org.suyash.order.messaging.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.suyash.common.events.DomainEvent;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.common.processor.EventProcessor;
import org.suyash.util.MessageConverter;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public abstract class AbstractOrderEventRouterConfig {

    private static final Logger log = LoggerFactory.getLogger(AbstractOrderEventRouterConfig.class);
    private static final String DESTINATION_HEADER = "spring.cloud.stream.sendto.destination";
    private static final String ORDER_EVENTS_CHANNEL = "order-events-channel";

    protected <T extends DomainEvent> Function<Flux<Message<T>>, Flux<Message<OrderEvent>>> processor(EventProcessor<T, OrderEvent> eventProcessor) {
        return flux -> flux.map(MessageConverter::toRecord)
                           .doOnNext(r -> log.info("order service received {}", r.message()))
                           .concatMap(r -> eventProcessor.process(r.message())
                                                              .doOnSuccess(e -> r.acknowledgement().acknowledge())
                           )
                           .map(this::toMessage);
    }

    protected Message<OrderEvent> toMessage(OrderEvent event) {
        log.info("order service produced {}", event);
        return MessageBuilder.withPayload(event)
                             .setHeader(KafkaHeaders.KEY, event.orderId().toString())
                             .setHeader(DESTINATION_HEADER, ORDER_EVENTS_CHANNEL)
                             .build();
    }
    
}
