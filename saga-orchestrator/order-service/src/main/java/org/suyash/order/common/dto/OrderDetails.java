package org.suyash.order.common.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record OrderDetails(PurchaseOrderDto order,
                           List<OrderWorkflowActionDto> actions) {
}
