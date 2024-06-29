package org.suyash.order.messaging.orchestrator;
import org.reactivestreams.Publisher;
import org.suyash.common.messages.Request;
import org.suyash.common.messages.inventory.InventoryResponse;
import org.suyash.common.orchestrator.WorkflowStep;

public interface InventoryStep extends WorkflowStep<InventoryResponse> {

    @Override
    default Publisher<Request> process(InventoryResponse response) {
        return switch (response){
            case InventoryResponse.Deducted r -> this.onSuccess(r);
            case InventoryResponse.Declined r -> this.onFailure(r);
        };
    }

    Publisher<Request> onSuccess(InventoryResponse.Deducted response);

    Publisher<Request> onFailure(InventoryResponse.Declined response);

}
