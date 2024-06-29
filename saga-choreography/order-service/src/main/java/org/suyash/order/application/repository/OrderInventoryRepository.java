package org.suyash.order.application.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.suyash.order.application.entity.OrderInventory;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrderInventoryRepository extends ReactiveCrudRepository<OrderInventory, Integer> {

    Mono<OrderInventory> findByOrderId(UUID orderId);

}
