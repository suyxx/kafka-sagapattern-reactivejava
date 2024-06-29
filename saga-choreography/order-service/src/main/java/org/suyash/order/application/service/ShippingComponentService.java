package org.suyash.order.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.common.events.order.OrderStatus;
import org.suyash.order.application.repository.PurchaseOrderRepository;
import org.suyash.order.common.dto.OrderShipmentSchedule;
import org.suyash.order.common.service.shipping.ShippingComponentStatusListener;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ShippingComponentService implements ShippingComponentStatusListener {

    private final PurchaseOrderRepository repository;

    @Override
    public Mono<Void> onSuccess(OrderShipmentSchedule message) {
        return this.repository.findByOrderIdAndStatus(message.orderId(), OrderStatus.COMPLETED)
                              .doOnNext(e -> e.setDeliveryDate(message.deliveryDate()))
                              .flatMap(this.repository::save)
                              .then();
    }

    @Override
    public Mono<Void> onFailure(OrderShipmentSchedule message) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> onRollback(OrderShipmentSchedule message) {
        return Mono.empty();
    }

}
