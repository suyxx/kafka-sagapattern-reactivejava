package org.suyash.shipping.common.dto;

import lombok.Builder;
import org.suyash.common.events.shipping.ShippingStatus;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ShipmentDto(UUID shipmentId,
                          UUID orderId,
                          Integer productId,
                          Integer customerId,
                          Integer quantity,
                          Instant expectedDelivery,
                          ShippingStatus status) {
}
