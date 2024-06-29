package org.suyash.inventory.messaging.config;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.suyash.common.events.inventory.InventoryEvent;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.common.processor.OrderEventProcessor;
import org.suyash.util.MessageConverter;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class OrderEventProcessorConfig {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProcessorConfig.class);
    private final OrderEventProcessor<InventoryEvent> eventProcessor;

    @Bean
    public Function<Flux<Message<OrderEvent>>, Flux<Message<InventoryEvent>>> processor(){
        return flux -> flux.map(MessageConverter::toRecord)
                .doOnNext(r -> log.info("inventory service received {}", r.message()))
                .concatMap(r -> this.eventProcessor.process(r.message())
                        .doOnSuccess(e -> r.acknowledgement().acknowledge()))
                .map(this::toMessage);
    }

    private Message<InventoryEvent> toMessage(InventoryEvent event){
        return MessageBuilder.withPayload(event)
                .setHeader(KafkaHeaders.KEY, event.orderId().toString())
                .build();
    }

}
