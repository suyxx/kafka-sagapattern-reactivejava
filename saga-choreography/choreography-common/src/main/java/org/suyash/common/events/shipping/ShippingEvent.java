package org.suyash.common.events.shipping;

import lombok.Builder;
import org.suyash.common.events.DomainEvent;
import org.suyash.common.events.OrderSaga;

import java.time.Instant;
import java.util.UUID;

public sealed interface ShippingEvent extends DomainEvent, OrderSaga {

    @Builder
    record ShippingScheduled(UUID orderId,
                             UUID shipmentId,
                             Instant expectedDelivery,
                             Instant createdAt) implements ShippingEvent{}
}
