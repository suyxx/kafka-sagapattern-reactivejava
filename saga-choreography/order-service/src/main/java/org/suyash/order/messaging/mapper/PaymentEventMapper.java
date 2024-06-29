package org.suyash.order.messaging.mapper;

import org.suyash.common.events.payment.PaymentEvent;
import org.suyash.common.events.payment.PaymentStatus;
import org.suyash.order.common.dto.OrderPaymentDto;

public class PaymentEventMapper {

    public static OrderPaymentDto toDto(PaymentEvent.PaymentDeducted event) {
        return OrderPaymentDto.builder()
                                    .orderId(event.orderId())
                                    .paymentId(event.paymentId())
                                    .status(PaymentStatus.DEDUCTED)
                                    .build();
    }

    public static OrderPaymentDto toDto(PaymentEvent.PaymentDeclined event) {
        return OrderPaymentDto.builder()
                                    .orderId(event.orderId())
                                    .status(PaymentStatus.DECLINED)
                                    .message(event.message())
                                    .build();
    }

    public static OrderPaymentDto toDto(PaymentEvent.PaymentRefunded event) {
        return OrderPaymentDto.builder()
                                    .orderId(event.orderId())
                                    .status(PaymentStatus.REFUNDED)
                                    .build();
    }

}
