package org.suyash.shipping.messaging.processor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.common.exception.EventAlreadyProcessedException;
import org.suyash.common.messages.shipping.ShippingRequest;
import org.suyash.common.messages.shipping.ShippingResponse;
import org.suyash.shipping.common.service.ShippingService;
import org.suyash.shipping.messaging.mapper.MessageDtoMapper;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class ShippingRequestProcessorImpl implements ShippingRequestProcessor {

    private final ShippingService service;

    @Override
    public Mono<ShippingResponse> handle(ShippingRequest.Schedule request) {
        var dto = MessageDtoMapper.toScheduleRequest(request);
        return this.service.schedule(dto)
                           .map(MessageDtoMapper::toScheduledResponse)
                           .transform(exceptionHandler(request));
    }

    private UnaryOperator<Mono<ShippingResponse>> exceptionHandler(ShippingRequest.Schedule request) {
        return mono -> mono.onErrorResume(EventAlreadyProcessedException.class, e -> Mono.empty())
                           .onErrorResume(MessageDtoMapper.toShippingDeclinedResponse(request));
    }

}
