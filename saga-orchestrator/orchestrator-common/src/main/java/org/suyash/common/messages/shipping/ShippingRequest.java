package org.suyash.common.messages.shipping;

import lombok.Builder;
import org.suyash.common.messages.Request;

import java.util.UUID;

public sealed interface ShippingRequest extends Request {

    @Builder
    record Schedule(UUID orderId,
                    Integer customerId,
                    Integer productId,
                    Integer quantity) implements ShippingRequest {

    }

}
