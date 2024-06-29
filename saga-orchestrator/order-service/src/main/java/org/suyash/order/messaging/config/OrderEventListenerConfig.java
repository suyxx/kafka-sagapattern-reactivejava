package org.suyash.order.messaging.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.suyash.order.common.service.OrderEventListener;
import org.suyash.order.messaging.publisher.OrderEventListenerImpl;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@Configuration
public class OrderEventListenerConfig {

    @Bean
    public OrderEventListener orderEventListener(){
        var sink = Sinks.many().unicast().<UUID>onBackpressureBuffer();
        var flux = sink.asFlux();
        return new OrderEventListenerImpl(sink, flux);
    }

}
