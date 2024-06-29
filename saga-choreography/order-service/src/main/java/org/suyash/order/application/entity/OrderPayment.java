package org.suyash.order.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.suyash.common.events.payment.PaymentStatus;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPayment {


    @Id
    private Integer id;
    private UUID orderId;
    private UUID paymentId;
    private PaymentStatus status;
    private Boolean success;
    private String message;

}
