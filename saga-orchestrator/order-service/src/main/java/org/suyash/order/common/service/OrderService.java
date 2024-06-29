package org.suyash.order.common.service;
import org.suyash.order.common.dto.OrderCreateRequest;
import org.suyash.order.common.dto.OrderDetails;
import org.suyash.order.common.dto.PurchaseOrderDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface OrderService {

    Mono<PurchaseOrderDto> placeOrder(OrderCreateRequest request);

    Flux<PurchaseOrderDto> getAllOrders();

    Mono<OrderDetails> getOrderDetails(UUID orderId);

}
