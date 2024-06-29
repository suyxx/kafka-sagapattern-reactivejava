package org.suyash.order.messaging.orchestrator;
import org.reactivestreams.Publisher;
import org.suyash.common.messages.Request;
import org.suyash.common.messages.Response;
import org.suyash.common.messages.inventory.InventoryResponse;
import org.suyash.common.messages.payment.PaymentResponse;
import org.suyash.common.messages.shipping.ShippingResponse;
import org.suyash.common.orchestrator.WorkflowOrchestrator;
import reactor.core.publisher.Mono;

public interface OrderFulfillmentOrchestrator extends WorkflowOrchestrator {

    Publisher<Request> orderInitialRequests();

    @Override
    default Publisher<Request> orchestrate(Response response) {
        return switch (response) {
            case PaymentResponse r -> this.handle(r);
            case InventoryResponse r -> this.handle(r);
            case ShippingResponse r -> this.handle(r);
            default -> Mono.empty();
        };
    }

    Publisher<Request> handle(PaymentResponse response);

    Publisher<Request> handle(InventoryResponse response);

    Publisher<Request> handle(ShippingResponse response);

}
