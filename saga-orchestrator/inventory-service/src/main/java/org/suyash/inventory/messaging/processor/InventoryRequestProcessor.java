package org.suyash.inventory.messaging.processor;
import org.suyash.common.messages.inventory.InventoryRequest;
import org.suyash.common.messages.inventory.InventoryResponse;
import org.suyash.common.processor.RequestProcessor;
import reactor.core.publisher.Mono;

public interface InventoryRequestProcessor extends RequestProcessor<InventoryRequest, InventoryResponse> {

    @Override
    default Mono<InventoryResponse> process(InventoryRequest request) {
        return switch (request){
            case InventoryRequest.Deduct r -> this.handle(r);
            case InventoryRequest.Restore r -> this.handle(r);
        };
    }

    Mono<InventoryResponse> handle(InventoryRequest.Deduct request);

    Mono<InventoryResponse> handle(InventoryRequest.Restore request);

}
