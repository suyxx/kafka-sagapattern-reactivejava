package org.suyash.shipping.common.dto;
import lombok.Builder;
import org.suyash.common.messages.shipping.ShippingStatus;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ShipmentDto(UUID shipmentId,
                          UUID orderId,
                          Integer productId,
                          Integer customerId,
                          Integer quantity,
                          Instant deliveryDate,
                          ShippingStatus status) {
}
