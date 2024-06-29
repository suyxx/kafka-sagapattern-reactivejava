package org.suyash.order.application.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.order.application.mapper.EntityDtoMapper;
import org.suyash.order.application.repository.PurchaseOrderRepository;
import org.suyash.order.common.dto.OrderCreateRequest;
import org.suyash.order.common.dto.OrderDetails;
import org.suyash.order.common.dto.PurchaseOrderDto;
import org.suyash.order.common.service.OrderEventListener;
import org.suyash.order.common.service.OrderService;
import org.suyash.order.common.service.WorkflowActionRetriever;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final PurchaseOrderRepository repository;
    private final OrderEventListener eventListener;
    private final WorkflowActionRetriever actionRetriever;

    @Override
    public Mono<PurchaseOrderDto> placeOrder(OrderCreateRequest request) {
        var entity = EntityDtoMapper.toPurchaseOrder(request);
        return this.repository.save(entity)
                              .map(EntityDtoMapper::toPurchaseOrderDto)
                              .doOnNext(eventListener::emitOrderCreated);
    }

    @Override
    public Flux<PurchaseOrderDto> getAllOrders() {
        return this.repository.findAll()
                              .map(EntityDtoMapper::toPurchaseOrderDto);
    }

    @Override
    public Mono<OrderDetails> getOrderDetails(UUID orderId) {
        return this.repository.findById(orderId)
                              .map(EntityDtoMapper::toPurchaseOrderDto)
                              .zipWith(this.actionRetriever.retrieve(orderId).collectList())
                              .map(t -> EntityDtoMapper.toOrderDetails(t.getT1(), t.getT2()));
    }

}
