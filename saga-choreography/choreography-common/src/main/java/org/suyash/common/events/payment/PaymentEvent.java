package org.suyash.common.events.payment;

import lombok.Builder;
import org.suyash.common.events.DomainEvent;
import org.suyash.common.events.OrderSaga;

import java.time.Instant;
import java.util.UUID;

public sealed interface PaymentEvent extends DomainEvent, OrderSaga {

    @Builder
    record PaymentDeducted(UUID orderId,
                           UUID paymentId,
                           Integer customerId,
                           Integer amount,
                           Instant createdAt) implements PaymentEvent {

    }

    @Builder
    record PaymentRefunded(UUID orderId,
                           UUID paymentId,
                           Integer customerId,
                           Integer amount,
                           Instant createdAt) implements PaymentEvent {

    }

    @Builder
    record PaymentDeclined(UUID orderId,
                           Integer customerId,
                           Integer amount,
                           String message,
                           Instant createdAt) implements PaymentEvent {

    }
}
