package org.suyash.order.common.dto;

import lombok.Builder;
import org.suyash.common.events.inventory.InventoryStatus;

import java.util.UUID;

@Builder
public record OrderInventoryDto(UUID orderId,
                                UUID inventoryId,
                                InventoryStatus status,
                                String message) {
}
