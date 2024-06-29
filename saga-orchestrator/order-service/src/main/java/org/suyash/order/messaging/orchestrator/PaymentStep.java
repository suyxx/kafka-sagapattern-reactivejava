package org.suyash.order.messaging.orchestrator;
import org.reactivestreams.Publisher;
import org.suyash.common.messages.Request;
import org.suyash.common.messages.payment.PaymentResponse;
import org.suyash.common.orchestrator.WorkflowStep;

public interface PaymentStep extends WorkflowStep<PaymentResponse> {

    @Override
    default Publisher<Request> process(PaymentResponse response) {
        return switch (response){
            case PaymentResponse.Processed r -> this.onSuccess(r);
            case PaymentResponse.Declined r -> this.onFailure(r);
        };
    }

    Publisher<Request> onSuccess(PaymentResponse.Processed response);

    Publisher<Request> onFailure(PaymentResponse.Declined response);

}
