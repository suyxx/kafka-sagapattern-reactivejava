package org.suyash.shipping.application.repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.suyash.shipping.application.entity.Shipment;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ShipmentRepository extends ReactiveCrudRepository<Shipment, UUID> {

    Mono<Boolean> existsByOrderId(UUID orderId);

}
