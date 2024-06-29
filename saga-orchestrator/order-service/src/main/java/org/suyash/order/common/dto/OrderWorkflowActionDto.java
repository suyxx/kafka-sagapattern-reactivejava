package org.suyash.order.common.dto;

import lombok.Builder;
import org.suyash.order.common.enums.WorkflowAction;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OrderWorkflowActionDto(UUID id,
                                     UUID orderId,
                                     WorkflowAction action,
                                     Instant createdAt) {
}
