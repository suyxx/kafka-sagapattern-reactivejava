package org.suyash.order.messaging.orchestrator;
import org.reactivestreams.Publisher;
import org.suyash.common.messages.Request;
import org.suyash.common.messages.shipping.ShippingResponse;
import org.suyash.common.orchestrator.WorkflowStep;

public interface ShippingStep extends WorkflowStep<ShippingResponse> {

    @Override
    default Publisher<Request> process(ShippingResponse response) {
        return switch (response){
            case ShippingResponse.Scheduled r -> this.onSuccess(r);
            case ShippingResponse.Declined r -> this.onFailure(r);
        };
    }

    Publisher<Request> onSuccess(ShippingResponse.Scheduled response);

    Publisher<Request> onFailure(ShippingResponse.Declined response);

}
