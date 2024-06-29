package org.suyash.payment.messaging.processor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.suyash.common.exception.EventAlreadyProcessedException;
import org.suyash.common.messages.payment.PaymentRequest;
import org.suyash.common.messages.payment.PaymentResponse;
import org.suyash.payment.common.service.PaymentService;
import org.suyash.payment.messaging.mapper.MessageDtoMapper;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
public class PaymentRequestProcessorImpl implements PaymentRequestProcessor {

    private final PaymentService service;

    @Override
    public Mono<PaymentResponse> handle(PaymentRequest.Process request) {
        var dto = MessageDtoMapper.toProcessRequest(request);
        return this.service.process(dto)
                           .map(MessageDtoMapper::toProcessedResponse)
                           .transform(exceptionHandler(request));
    }

    @Override
    public Mono<PaymentResponse> handle(PaymentRequest.Refund request) {
        return this.service.refund(request.orderId())
                           .then(Mono.empty());
    }

    private UnaryOperator<Mono<PaymentResponse>> exceptionHandler(PaymentRequest.Process request) {
        return mono -> mono.onErrorResume(EventAlreadyProcessedException.class, ex -> Mono.empty())
                           .onErrorResume(MessageDtoMapper.toPaymentDeclinedResponse(request));
    }

}
