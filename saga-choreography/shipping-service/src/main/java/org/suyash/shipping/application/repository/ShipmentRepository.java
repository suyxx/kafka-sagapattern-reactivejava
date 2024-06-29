package org.suyash.shipping.application.repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.suyash.common.events.shipping.ShippingStatus;
import org.suyash.shipping.application.entity.Shipment;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ShipmentRepository extends ReactiveCrudRepository<Shipment, UUID> {

    Mono<Boolean> existsByOrderId(UUID orderId);

    Mono<Shipment> findByOrderIdAndStatus(UUID orderId, ShippingStatus status);

    Mono<Void> deleteByOrderId(UUID orderId);

}
