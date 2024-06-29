package org.suyash.inventory.common.dto;

import lombok.Builder;
import org.suyash.common.events.inventory.InventoryStatus;

import java.util.UUID;

@Builder
public record OrderInventoryDto(
        UUID inventoryId,
        UUID orderId,
        Integer productId,
        Integer quantity,
        InventoryStatus status
){}
