package org.suyash.order.application.repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.suyash.order.application.entity.OrderWorkflowAction;
import org.suyash.order.common.enums.WorkflowAction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrderWorkflowActionRepository extends ReactiveCrudRepository<OrderWorkflowAction, UUID> {

    Mono<Boolean> existsByOrderIdAndAction(UUID orderId, WorkflowAction action);

    Flux<OrderWorkflowAction> findByOrderIdOrderByCreatedAt(UUID orderId);

}
