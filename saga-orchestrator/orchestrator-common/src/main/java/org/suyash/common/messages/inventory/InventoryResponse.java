package org.suyash.common.messages.inventory;

import lombok.Builder;
import org.suyash.common.messages.Response;

import java.util.UUID;

public sealed interface InventoryResponse extends Response {


    @Builder
    record Deducted(UUID orderId,
                     UUID inventoryId,
                     Integer productId,
                     Integer quantity) implements InventoryResponse {

    }

    @Builder
    record Declined(UUID orderId,
                    String message) implements InventoryResponse {

    }

}
