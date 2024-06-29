package org.suyash.common.messages.payment;

import lombok.Builder;
import org.suyash.common.messages.Request;

import java.util.UUID;

public sealed interface PaymentRequest extends Request {


    @Builder
    record Process(UUID orderId,
                   Integer customerId,
                   Integer amount) implements PaymentRequest {
    }

    @Builder
    record Refund(UUID orderId) implements PaymentRequest {
    }

}
