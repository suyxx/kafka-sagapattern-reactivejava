package org.suyash.inventory.messaging.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.common.exception.EventAlreadyProcessedException;
import org.suyash.common.messages.inventory.InventoryRequest;
import org.suyash.common.messages.inventory.InventoryResponse;
import org.suyash.inventory.common.service.InventoryService;
import org.suyash.inventory.messaging.mapper.MessageDtoMapper;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class InventoryRequestProcessorImpl implements InventoryRequestProcessor {

    private final InventoryService service;

    @Override
    public Mono<InventoryResponse> handle(InventoryRequest.Deduct request) {
        return this.service.deduct(MessageDtoMapper.toInventoryDeductRequest(request))
                           .map(MessageDtoMapper::toInventoryDeductedResponse)
                           .transform(exceptionHandler(request));
    }

    @Override
    public Mono<InventoryResponse> handle(InventoryRequest.Restore request) {
        return this.service.restore(request.orderId())
                           .then(Mono.empty());
    }

    private UnaryOperator<Mono<InventoryResponse>> exceptionHandler(InventoryRequest.Deduct request) {
        return mono -> mono.onErrorResume(EventAlreadyProcessedException.class, e -> Mono.empty())
                           .onErrorResume(MessageDtoMapper.toInventoryDeclinedResponse(request));
    }

}
