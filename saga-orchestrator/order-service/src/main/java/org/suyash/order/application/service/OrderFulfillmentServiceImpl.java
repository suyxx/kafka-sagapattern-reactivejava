package org.suyash.order.application.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.order.application.entity.PurchaseOrder;
import org.suyash.order.application.mapper.EntityDtoMapper;
import org.suyash.order.application.repository.PurchaseOrderRepository;
import org.suyash.order.common.dto.OrderShipmentSchedule;
import org.suyash.order.common.dto.PurchaseOrderDto;
import org.suyash.order.common.enums.OrderStatus;
import org.suyash.order.common.service.OrderFulfillmentService;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class OrderFulfillmentServiceImpl implements OrderFulfillmentService {

    private final PurchaseOrderRepository repository;

    @Override
    public Mono<PurchaseOrderDto> get(UUID orderId) {
        return this.repository.findById(orderId)
                              .map(EntityDtoMapper::toPurchaseOrderDto);
    }

    @Override
    public Mono<PurchaseOrderDto> schedule(OrderShipmentSchedule shipmentSchedule) {
        return this.update(shipmentSchedule.orderId(), e -> e.setDeliveryDate(shipmentSchedule.deliveryDate()));
    }

    @Override
    public Mono<PurchaseOrderDto> complete(UUID orderId) {
        return this.update(orderId, e -> e.setStatus(OrderStatus.COMPLETED));
    }

    @Override
    public Mono<PurchaseOrderDto> cancel(UUID orderId) {
        return this.update(orderId, e -> e.setStatus(OrderStatus.CANCELLED));
    }

    private Mono<PurchaseOrderDto> update(UUID orderId, Consumer<PurchaseOrder> consumer) {
        return this.repository.findByOrderIdAndStatus(orderId, OrderStatus.PENDING)
                              .doOnNext(consumer)
                              .flatMap(this.repository::save)
                              .map(EntityDtoMapper::toPurchaseOrderDto);
    }

}
