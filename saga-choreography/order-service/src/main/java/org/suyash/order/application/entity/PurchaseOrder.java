package org.suyash.order.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.suyash.common.events.order.OrderStatus;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {

    @Id
    private UUID orderId;
    private Integer customerId;
    private Integer productId;
    private Integer quantity;
    private Integer unitPrice;
    private Integer amount;
    private OrderStatus status;
    private Instant deliveryDate;

    @Version
    private Integer version;
}
