package org.suyash.order.messaging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.order.common.service.OrderEventListener;
import org.suyash.order.messaging.publisher.OrderEventListenerImpl;
import reactor.core.publisher.Sinks;

@Configuration
public class OrderEventListenerConfig {

    @Bean
    public OrderEventListener orderEventListener(){
        var sink = Sinks.many().unicast().<OrderEvent>onBackpressureBuffer();
        var flux = sink.asFlux();
        return new OrderEventListenerImpl(sink, flux);
    }

}
