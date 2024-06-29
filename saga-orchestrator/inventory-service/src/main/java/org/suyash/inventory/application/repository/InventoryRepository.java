package org.suyash.inventory.application.repository;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.suyash.common.messages.inventory.InventoryStatus;
import org.suyash.inventory.application.entity.OrderInventory;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface InventoryRepository extends ReactiveCrudRepository<OrderInventory, UUID> {

    Mono<Boolean> existsByOrderId(UUID orderId);

    Mono<OrderInventory> findByOrderIdAndStatus(UUID orderId, InventoryStatus status);

}
