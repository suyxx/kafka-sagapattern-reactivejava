package org.suyash.order.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.order.application.entity.OrderInventory;
import org.suyash.order.application.mapper.EntityDtoMapper;
import org.suyash.order.application.repository.OrderInventoryRepository;
import org.suyash.order.common.dto.OrderInventoryDto;
import org.suyash.order.common.service.inventory.InventoryComponentFetcher;
import org.suyash.order.common.service.inventory.InventoryComponentStatusListener;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryComponentService implements InventoryComponentFetcher, InventoryComponentStatusListener {

    private static final OrderInventoryDto DEFAULT = OrderInventoryDto.builder().build();
    private final OrderInventoryRepository repository;

    @Override
    public Mono<OrderInventoryDto> getComponent(UUID orderId) {
        return this.repository.findByOrderId(orderId)
                              .map(EntityDtoMapper::toOrderInventoryDto)
                              .defaultIfEmpty(DEFAULT);
    }

    @Override
    public Mono<Void> onSuccess(OrderInventoryDto message) {
        return this.repository.findByOrderId(message.orderId())
                              .switchIfEmpty(Mono.defer(() -> this.add(message, true)))
                              .then();
    }

    @Override
    public Mono<Void> onFailure(OrderInventoryDto message) {
        return this.repository.findByOrderId(message.orderId())
                              .switchIfEmpty(Mono.defer(() -> this.add(message, false)))
                              .then();
    }

    @Override
    public Mono<Void> onRollback(OrderInventoryDto message) {
        return this.repository.findByOrderId(message.orderId())
                              .doOnNext(e -> e.setStatus(message.status()))
                              .flatMap(this.repository::save)
                              .then();
    }

    private Mono<OrderInventory> add(OrderInventoryDto dto, boolean isSuccess) {
        var entity = EntityDtoMapper.toOrderInventory(dto);
        entity.setSuccess(isSuccess);
        return this.repository.save(entity);
    }

}
