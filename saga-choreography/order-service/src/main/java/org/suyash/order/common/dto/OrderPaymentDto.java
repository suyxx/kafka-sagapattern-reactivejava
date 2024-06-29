package org.suyash.order.common.dto;

import lombok.Builder;
import org.suyash.common.events.payment.PaymentStatus;

import java.util.UUID;

@Builder
public record OrderPaymentDto(UUID orderId,
                              UUID paymentId,
                              PaymentStatus status,
                              String message) {
}
