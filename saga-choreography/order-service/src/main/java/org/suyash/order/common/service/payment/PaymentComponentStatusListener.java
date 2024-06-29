package org.suyash.order.common.service.payment;


import org.suyash.order.common.dto.OrderPaymentDto;
import org.suyash.order.common.service.OrderComponentStatusListener;

public interface PaymentComponentStatusListener extends OrderComponentStatusListener<OrderPaymentDto> {
}
