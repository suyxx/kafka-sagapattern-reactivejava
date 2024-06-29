package org.suyash.common.messages.inventory;

import lombok.Builder;
import org.suyash.common.messages.Request;

import java.util.UUID;

public sealed interface InventoryRequest extends Request {

    @Builder
    record Deduct(UUID orderId,
                  Integer productId,
                  Integer quantity) implements InventoryRequest {

    }

    @Builder
    record Restore(UUID orderId) implements InventoryRequest {

    }

}
