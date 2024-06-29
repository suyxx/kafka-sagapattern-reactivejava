package com.suyash.payment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.common.events.payment.PaymentEvent;
import org.suyash.payment.CustomerPaymentApplication;
import org.suyash.payment.application.repository.CustomerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SpringBootTest(classes = {CustomerPaymentApplication.class, PaymentServiceTest.TestConfig.class}) // Reference main application and test config

@TestPropertySource(properties = {
        "spring.cloud.function.definition=processor;orderEventProducer;paymentEventConsumer",
        "spring.cloud.stream.bindings.orderEventProducer-out-0.destination=order-events",
        "spring.cloud.stream.bindings.paymentEventConsumer-in-0.destination=payment-events"
})
public class PaymentServiceTest extends AbstractIntegrationTest {

    private static final Sinks.Many<OrderEvent> reqSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Sinks.Many<PaymentEvent> resSink = Sinks.many().unicast().onBackpressureBuffer();
    private static final Flux<PaymentEvent> resFlux = resSink.asFlux().cache(0);

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void processPaymentTest(){
        var orderCreatedEvent = TestDataUtil.createOrderCreatedEvent(1,1,2,3);

        expectEvent(orderCreatedEvent, PaymentEvent.PaymentDeducted.class, e -> {
            Assertions.assertNotNull(e.paymentId());
            Assertions.assertEquals(orderCreatedEvent.orderId(), e.orderId());
            Assertions.assertEquals(6, e.amount());
        });


        //check balance
        this.customerRepository.findById(1)
                .as(StepVerifier::create)
                .consumeNextWith(c -> Assertions.assertEquals(94, c.getBalance()))
                .verifyComplete();

        //duplicate event
        noExpectEvent(orderCreatedEvent);


        //cancel and refund
        var cancelledEvent = TestDataUtil.createOrderCancelledEvent(orderCreatedEvent.orderId());
        expectEvent(cancelledEvent, PaymentEvent.PaymentRefunded.class, e -> {
            Assertions.assertNotNull(e.paymentId());
            Assertions.assertEquals(orderCreatedEvent.orderId(), e.orderId());
            Assertions.assertEquals(6, e.amount());
        });

        //check balance
        this.customerRepository.findById(1)
                .as(StepVerifier::create)
                .consumeNextWith(c -> Assertions.assertEquals(100, c.getBalance()))
                .verifyComplete();

    }

    @Test
    public void refundWithoutDeductTest(){
        var cancelledEvent = TestDataUtil.createOrderCancelledEvent(UUID.randomUUID());
        noExpectEvent(cancelledEvent);
    }

    @Test
    public void customerNotFoundTest(){
        var orderCreatedEvent = TestDataUtil.createOrderCreatedEvent(10,1,2,3);
        expectEvent(orderCreatedEvent, PaymentEvent.PaymentDeclined.class,e -> {
            Assertions.assertEquals(orderCreatedEvent.orderId(), e.orderId());
            Assertions.assertEquals(6, e.amount());
            Assertions.assertEquals("Customer not found", e.message());
        });
    }

    @Test
    public void insufficientBalanceTest(){
        var orderCreatedEvent = TestDataUtil.createOrderCreatedEvent(1,1,2,51);
        expectEvent(orderCreatedEvent, PaymentEvent.PaymentDeclined.class, e -> {
            Assertions.assertEquals(orderCreatedEvent.orderId(), e.orderId());
            Assertions.assertEquals(102, e.amount());
            Assertions.assertEquals("Customer does not have enough balance", e.message());
        });
    }

    private <T> void expectEvent(OrderEvent event, Class<T> type, Consumer<T> assertion){
        resFlux
                .doFirst( () -> reqSink.tryEmitNext(event))
                .next()
                .timeout(Duration.ofSeconds(3), Mono.empty())
                .cast(type)
                .as(StepVerifier::create)
                .consumeNextWith(assertion)
                .verifyComplete();
    }

    private <T> void noExpectEvent(OrderEvent event){
        resFlux
                .doFirst( () -> reqSink.tryEmitNext(event))
                .next()
                .timeout(Duration.ofSeconds(30), Mono.empty())
                .as(StepVerifier::create)
                .verifyComplete();
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public Supplier<Flux<OrderEvent>> orderEventProducer(){
            return reqSink::asFlux;
        }

        @Bean
        public Consumer<Flux<PaymentEvent>> paymentEventConsumer(){
            return f -> f.doOnNext(resSink::tryEmitNext).subscribe();
        }

    }
}
