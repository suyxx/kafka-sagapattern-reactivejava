package org.suyash.order.application.service;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.suyash.common.events.order.OrderStatus;
import org.suyash.order.application.entity.PurchaseOrder;
import org.suyash.order.application.mapper.EntityDtoMapper;
import org.suyash.order.application.repository.PurchaseOrderRepository;
import org.suyash.order.common.dto.PurchaseOrderDto;
import org.suyash.order.common.service.OrderFulfillmentService;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService {

    private final PurchaseOrderRepository repository;

    @Override
    public Mono<PurchaseOrderDto> complete(UUID orderId) {
        return this.repository.getWhenOrderComponentsCompleted(orderId)
                              .transform(this.updateStatus(OrderStatus.COMPLETED));
    }

    @Override
    public Mono<PurchaseOrderDto> cancel(UUID orderId) {
        return this.repository.findByOrderIdAndStatus(orderId, OrderStatus.PENDING)
                              .transform(this.updateStatus(OrderStatus.CANCELLED));
    }

    private Function<Mono<PurchaseOrder>, Mono<PurchaseOrderDto>> updateStatus(OrderStatus status) {
        return mono -> mono
                .doOnNext(e -> e.setStatus(status))
                .flatMap(this.repository::save)
                .retryWhen(Retry.max(1).filter(OptimisticLockingFailureException.class::isInstance))
                .map(EntityDtoMapper::toPurchaseOrderDto);
    }

}
