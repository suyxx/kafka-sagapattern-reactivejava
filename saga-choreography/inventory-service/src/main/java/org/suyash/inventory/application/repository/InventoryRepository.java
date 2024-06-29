package org.suyash.inventory.application.repository;

import org.springframework.core.annotation.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.suyash.common.events.inventory.InventoryStatus;
import org.suyash.inventory.application.entity.OrderInventory;
import org.suyash.inventory.common.service.InventoryService;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface InventoryRepository extends ReactiveCrudRepository<OrderInventory, UUID> {

    Mono<Boolean> existsByOrderId(UUID orderId);

    Mono<OrderInventory> findByOrderIdAndStatus(UUID orderId, InventoryStatus status);
}
