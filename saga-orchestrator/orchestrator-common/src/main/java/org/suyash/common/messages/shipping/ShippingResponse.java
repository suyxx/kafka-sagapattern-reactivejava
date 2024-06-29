package org.suyash.common.messages.shipping;

import lombok.Builder;
import org.suyash.common.messages.Response;

import java.time.Instant;
import java.util.UUID;

public sealed interface ShippingResponse extends Response {


    @Builder
    record Scheduled(UUID orderId,
                     UUID shipmentId,
                     Instant deliveryDate) implements ShippingResponse {

    }

    @Builder
    record Declined(UUID orderId,
                    String message) implements ShippingResponse {

    }

}
