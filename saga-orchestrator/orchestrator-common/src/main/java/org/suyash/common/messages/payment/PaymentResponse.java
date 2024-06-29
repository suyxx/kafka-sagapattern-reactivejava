package org.suyash.common.messages.payment;

import lombok.Builder;
import org.suyash.common.messages.Response;

import java.util.UUID;

public sealed interface PaymentResponse extends Response {


    @Builder
    record Processed(UUID orderId,
                     UUID paymentId,
                     Integer customerId,
                     Integer amount) implements PaymentResponse {

    }

    @Builder
    record Declined(UUID orderId,
                    String message) implements PaymentResponse {

    }

}
