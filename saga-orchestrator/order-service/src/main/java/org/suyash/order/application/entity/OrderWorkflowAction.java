package org.suyash.order.application.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.suyash.order.common.enums.WorkflowAction;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderWorkflowAction {

    @Id
    private UUID id;
    private UUID orderId;
    private WorkflowAction action;
    private Instant createdAt;

}
