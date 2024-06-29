package org.suyash.order.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.order.application.entity.OrderPayment;
import org.suyash.order.application.mapper.EntityDtoMapper;
import org.suyash.order.application.repository.OrderPaymentRepository;
import org.suyash.order.common.dto.OrderPaymentDto;
import org.suyash.order.common.service.payment.PaymentComponentFetcher;
import org.suyash.order.common.service.payment.PaymentComponentStatusListener;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentComponentService implements PaymentComponentFetcher, PaymentComponentStatusListener {

    private static final OrderPaymentDto DEFAULT = OrderPaymentDto.builder().build();
    private final OrderPaymentRepository repository;

    @Override
    public Mono<OrderPaymentDto> getComponent(UUID orderId) {
        return this.repository.findByOrderId(orderId)
                              .map(EntityDtoMapper::toOrderPaymentDto)
                              .defaultIfEmpty(DEFAULT);
    }

    @Override
    public Mono<Void> onSuccess(OrderPaymentDto message) {
        return this.repository.findByOrderId(message.orderId())
                              .switchIfEmpty(Mono.defer(() -> this.add(message, true)))
                              .then();
    }

    @Override
    public Mono<Void> onFailure(OrderPaymentDto message) {
        return this.repository.findByOrderId(message.orderId())
                              .switchIfEmpty(Mono.defer(() -> this.add(message, false)))
                              .then();
    }

    @Override
    public Mono<Void> onRollback(OrderPaymentDto message) {
        return this.repository.findByOrderId(message.orderId())
                              .doOnNext(e -> e.setStatus(message.status()))
                              .flatMap(this.repository::save)
                              .then();
    }

    private Mono<OrderPayment> add(OrderPaymentDto dto, boolean isSuccess) {
        var entity = EntityDtoMapper.toOrderPayment(dto);
        entity.setSuccess(isSuccess);
        return this.repository.save(entity);
    }

}
