package org.suyash.common.events.inventory;

import lombok.Builder;
import org.apache.kafka.common.protocol.types.Field;
import org.suyash.common.events.DomainEvent;
import org.suyash.common.events.OrderSaga;

import java.time.Instant;
import java.util.UUID;

public sealed interface InventoryEvent extends DomainEvent, OrderSaga {

    @Builder
    record InventoryDeducted(
            UUID orderId,
            UUID inventoryId,
            Integer productId,
            Integer quantity,
            Instant createdAt
    ) implements  InventoryEvent{}

    @Builder
    record InventoryRestored(
            UUID orderId,
            UUID inventoryId,
            Integer productId,
            Integer quantity,
            Instant createdAt
    ) implements  InventoryEvent{}

    @Builder
    record InventoryDeclined(
            UUID orderId,
            Integer productId,
            Integer quantity,
            String message,
            Instant createdAt
    ) implements  InventoryEvent{}
}
