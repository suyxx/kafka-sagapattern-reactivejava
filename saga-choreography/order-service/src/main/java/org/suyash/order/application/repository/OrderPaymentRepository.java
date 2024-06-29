package org.suyash.order.application.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.suyash.order.application.entity.OrderPayment;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrderPaymentRepository extends ReactiveCrudRepository<OrderPayment, Integer> {

    Mono<OrderPayment> findByOrderId(UUID orderId);

}
