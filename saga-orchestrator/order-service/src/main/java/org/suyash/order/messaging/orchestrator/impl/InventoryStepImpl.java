package org.suyash.order.messaging.orchestrator.impl;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.suyash.common.messages.Request;
import org.suyash.common.messages.inventory.InventoryResponse;
import org.suyash.common.orchestrator.RequestCompensator;
import org.suyash.common.orchestrator.RequestSender;
import org.suyash.order.common.enums.WorkflowAction;
import org.suyash.order.common.service.OrderFulfillmentService;
import org.suyash.order.common.service.WorkflowActionTracker;
import org.suyash.order.messaging.mapper.MessageDtoMapper;
import org.suyash.order.messaging.orchestrator.InventoryStep;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryStepImpl implements InventoryStep {

    private final WorkflowActionTracker tracker;
    private final OrderFulfillmentService service;
    private RequestCompensator previousStep;
    private RequestSender nextStep;

    @Override
    public Publisher<Request> compensate(UUID orderId) {
        return this.tracker.track(orderId, WorkflowAction.INVENTORY_RESTORE_INITIATED)
                           .<Request>thenReturn(MessageDtoMapper.toInventoryRestoreRequest(orderId))
                           .concatWith(this.previousStep.compensate(orderId));
    }

    @Override
    public Publisher<Request> send(UUID orderId) {
        return this.tracker.track(orderId, WorkflowAction.INVENTORY_REQUEST_INITIATED)
                           .then(this.service.get(orderId))
                           .map(MessageDtoMapper::toInventoryDeductRequest);
    }

    @Override
    public void setPreviousStep(RequestCompensator previousStep) {
        this.previousStep = previousStep;
    }

    @Override
    public void setNextStep(RequestSender nextStep) {
        this.nextStep = nextStep;
    }

    @Override
    public Publisher<Request> onSuccess(InventoryResponse.Deducted response) {
        return this.tracker.track(response.orderId(), WorkflowAction.INVENTORY_DEDUCTED)
                           .thenMany(this.nextStep.send(response.orderId()));
        // also Mono.from(...) can be used if we know for sure it is going to be only one request
    }

    @Override
    public Publisher<Request> onFailure(InventoryResponse.Declined response) {
        return this.tracker.track(response.orderId(), WorkflowAction.INVENTORY_DECLINED)
                           .thenMany(this.previousStep.compensate(response.orderId()));
    }
}
