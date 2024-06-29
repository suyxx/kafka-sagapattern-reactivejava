package org.suyash.inventory.messaging.config;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.suyash.common.messages.inventory.InventoryRequest;
import org.suyash.common.messages.inventory.InventoryResponse;
import org.suyash.common.util.MessageConverter;
import org.suyash.inventory.messaging.processor.InventoryRequestProcessor;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class InventoryRequestProcessorConfig {

    private static final Logger log = LoggerFactory.getLogger(InventoryRequestProcessorConfig.class);
    private final InventoryRequestProcessor inventoryRequestProcessor;

    @Bean
    public Function<Flux<Message<InventoryRequest>>, Flux<Message<InventoryResponse>>> processor() {
        return flux -> flux.map(MessageConverter::toRecord)
                           .doOnNext(r -> log.info("inventory service received {}", r.message()))
                           .concatMap(r -> this.inventoryRequestProcessor.process(r.message())
                                                                       .doOnSuccess(e -> r.acknowledgement().acknowledge())
                                                                       .doOnError(e -> log.error(e.getMessage()))
                           )
                           .map(this::toMessage);
    }

    private Message<InventoryResponse> toMessage(InventoryResponse response) {
        log.info("inventory service produced {}", response);
        return MessageBuilder.withPayload(response)
                             .setHeader(KafkaHeaders.KEY, response.orderId().toString())
                             .build();
    }

}
