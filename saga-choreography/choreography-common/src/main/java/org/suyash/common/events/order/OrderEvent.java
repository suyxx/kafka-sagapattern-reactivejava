package org.suyash.common.events.order;

import lombok.Builder;
import org.suyash.common.events.DomainEvent;
import org.suyash.common.events.OrderSaga;

import java.time.Instant;
import java.util.UUID;

public sealed interface OrderEvent extends DomainEvent, OrderSaga {
    @Builder
    record OrderCreated(UUID orderId,
                        Integer productId,
                        Integer customerId,
                        Integer quantity,
                        Integer unitPrice,
                        Integer totalAmount,
                        Instant createdAt) implements OrderEvent{}

    @Builder
    record OrderCancelled(UUID orderId,
                        String message,
                        Instant createdAt) implements OrderEvent{}

    @Builder
    record OrderCompleted(UUID orderId,
                          String message,
                          Instant createdAt) implements OrderEvent{}


}
