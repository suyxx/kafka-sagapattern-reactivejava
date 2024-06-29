package org.suyash.payment.messaging.mapper;

import org.suyash.common.events.order.OrderEvent;
import org.suyash.common.events.payment.PaymentEvent;
import org.suyash.payment.common.dto.PaymentDto;
import org.suyash.payment.common.dto.PaymentProcessRequest;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.Month;
import java.util.function.Function;

public class MessageDtoMapper {

    public static PaymentProcessRequest toPaymentProcessRequest(OrderEvent.OrderCreated event){
        return PaymentProcessRequest.builder()
                .customerId(event.customerId())
                .orderId(event.orderId())
                .amount(event.totalAmount())
                .build();
    }

    public static PaymentEvent toPaymentDeductedEvent(PaymentDto dto){
        return PaymentEvent.PaymentDeducted.builder()
                .paymentId(dto.paymentId())
                .orderId(dto.orderId())
                .amount(dto.amount())
                .customerId(dto.customerId())
                .createdAt(Instant.now())
                .build();
    }

    public static PaymentEvent toPaymentRefundedEvent(PaymentDto dto){
        return PaymentEvent.PaymentRefunded.builder()
                .paymentId(dto.paymentId())
                .orderId(dto.orderId())
                .amount(dto.amount())
                .customerId(dto.customerId())
                .createdAt(Instant.now())
                .build();
    }

    public static Function<Throwable, Mono<PaymentEvent>> toPaymentDeclinedEvent(OrderEvent.OrderCreated event){
        return ex -> Mono.fromSupplier(() -> PaymentEvent.PaymentDeclined.builder()
                .orderId(event.orderId())
                .amount(event.totalAmount())
                .customerId(event.customerId())
                .createdAt(Instant.now())
                .message(ex.getMessage())
                .build()
        );
    }
}
