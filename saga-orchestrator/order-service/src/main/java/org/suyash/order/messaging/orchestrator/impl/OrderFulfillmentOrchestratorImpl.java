package org.suyash.order.messaging.orchestrator.impl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.suyash.common.messages.Request;
import org.suyash.common.messages.inventory.InventoryResponse;
import org.suyash.common.messages.payment.PaymentResponse;
import org.suyash.common.messages.shipping.ShippingResponse;
import org.suyash.common.publisher.EventPublisher;
import org.suyash.order.common.service.OrderFulfillmentService;
import org.suyash.order.messaging.orchestrator.InventoryStep;
import org.suyash.order.messaging.orchestrator.OrderFulfillmentOrchestrator;
import org.suyash.order.messaging.orchestrator.PaymentStep;
import org.suyash.order.messaging.orchestrator.ShippingStep;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentOrchestratorImpl implements OrderFulfillmentOrchestrator {

    private final PaymentStep paymentStep;
    private final InventoryStep inventoryStep;
    private final ShippingStep shippingStep;
    private final OrderFulfillmentService service;
    private final EventPublisher<UUID> eventPublisher;
    private Workflow workflow;

    @PostConstruct
    private void init() {
        this.workflow = Workflow.startWith(paymentStep)
                                .thenNext(inventoryStep)
                                .thenNext(shippingStep)
                                .doOnFailure(id -> this.service.cancel(id).then())
                                .doOnSuccess(id -> this.service.complete(id).then()); // last step. or create it as builder

//        this.paymentStep.setPreviousStep(id -> this.service.cancel(id).then(Mono.empty()));
//        this.paymentStep.setNextStep(inventoryStep);
//        this.inventoryStep.setPreviousStep(paymentStep);
//        this.inventoryStep.setNextStep(shippingStep);
//        this.shippingStep.setPreviousStep(inventoryStep);
//        this.shippingStep.setNextStep(id -> this.service.complete(id).then(Mono.empty()));
    }

    @Override
    public Publisher<Request> orderInitialRequests() {
        return this.eventPublisher.publish()
                                  .flatMap(this.workflow.getFirstStep()::send);
    }

    @Override
    public Publisher<Request> handle(PaymentResponse response) {
        return this.paymentStep.process(response);
    }

    @Override
    public Publisher<Request> handle(InventoryResponse response) {
        return this.inventoryStep.process(response);
    }

    @Override
    public Publisher<Request> handle(ShippingResponse response) {
        return this.shippingStep.process(response);
    }

}
