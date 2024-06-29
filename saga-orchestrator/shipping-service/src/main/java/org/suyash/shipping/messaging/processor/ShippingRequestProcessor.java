package org.suyash.shipping.messaging.processor;
import org.suyash.common.messages.shipping.ShippingRequest;
import org.suyash.common.messages.shipping.ShippingResponse;
import org.suyash.common.processor.RequestProcessor;
import reactor.core.publisher.Mono;

public interface ShippingRequestProcessor extends RequestProcessor<ShippingRequest, ShippingResponse> {

    @Override
    default Mono<ShippingResponse> process(ShippingRequest request) {
        return switch (request){
            case ShippingRequest.Schedule s -> this.handle(s);
        };
    }

    Mono<ShippingResponse> handle(ShippingRequest.Schedule request);

}
