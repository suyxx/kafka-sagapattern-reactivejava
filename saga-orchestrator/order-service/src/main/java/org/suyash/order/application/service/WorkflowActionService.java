package org.suyash.order.application.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.common.util.DuplicateEventValidator;
import org.suyash.order.application.mapper.EntityDtoMapper;
import org.suyash.order.application.repository.OrderWorkflowActionRepository;
import org.suyash.order.common.dto.OrderWorkflowActionDto;
import org.suyash.order.common.enums.WorkflowAction;
import org.suyash.order.common.service.WorkflowActionRetriever;
import org.suyash.order.common.service.WorkflowActionTracker;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkflowActionService implements WorkflowActionTracker, WorkflowActionRetriever {

    private final OrderWorkflowActionRepository repository;

    @Override
    public Flux<OrderWorkflowActionDto> retrieve(UUID orderId) {
        return this.repository.findByOrderIdOrderByCreatedAt(orderId)
                              .map(EntityDtoMapper::toOrderWorkflowActionDto);
    }

    @Override
    public Mono<Void> track(UUID orderId, WorkflowAction action) {
        return DuplicateEventValidator.validate(
                this.repository.existsByOrderIdAndAction(orderId, action),
                this.repository.save(EntityDtoMapper.toOrderWorkflowAction(orderId, action)) // defer if required
        ).then();
    }
}
