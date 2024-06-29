package org.suyash.order.messaging.mapper;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.order.common.dto.PurchaseOrderDto;

import java.time.Instant;

public class OrderEventMapper {

    public static OrderEvent toOrderCreatedEvent(PurchaseOrderDto dto) {
        return OrderEvent.OrderCreated.builder()
                                      .orderId(dto.orderId())
                                      .unitPrice(dto.unitPrice())
                                      .quantity(dto.quantity())
                                      .productId(dto.productId())
                                      .totalAmount(dto.amount())
                                      .customerId(dto.customerId())
                                      .createdAt(Instant.now())
                                      .build();
    }

    public static OrderEvent toOrderCancelledEvent(PurchaseOrderDto dto) {
        return OrderEvent.OrderCancelled.builder()
                                        .orderId(dto.orderId())
                                        .createdAt(Instant.now())
                                        .build();
    }

    public static OrderEvent toOrderCompletedEvent(PurchaseOrderDto dto) {
        return OrderEvent.OrderCompleted.builder()
                                        .orderId(dto.orderId())
                                        .createdAt(Instant.now())
                                        .build();
    }

}
