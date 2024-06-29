package org.suyash.payment.common.dto;

import lombok.Builder;
import org.suyash.common.messages.payment.PaymentStatus;

import java.util.UUID;

@Builder
public record PaymentDto(UUID paymentId,
                         UUID orderId,
                         Integer customerId,
                         Integer amount,
                         PaymentStatus status) {
}
