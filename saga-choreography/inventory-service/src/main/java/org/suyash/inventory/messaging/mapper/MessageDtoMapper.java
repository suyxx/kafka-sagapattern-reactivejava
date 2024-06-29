package org.suyash.inventory.messaging.mapper;

import org.suyash.common.events.inventory.InventoryEvent;
import org.suyash.common.events.order.OrderEvent;
import org.suyash.inventory.common.dto.InventoryDeductRequest;
import org.suyash.inventory.common.dto.OrderInventoryDto;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.Function;

public class MessageDtoMapper {

    public static InventoryDeductRequest toInventoryDeductRequest(OrderEvent.OrderCreated event){
        return InventoryDeductRequest.builder()
                .orderId(event.orderId())
                .productId(event.productId())
                .quality(event.quantity())
                .build();
    }

    public static InventoryEvent toInventoryDeductEvent(OrderInventoryDto orderInventoryDto){
        return InventoryEvent.InventoryDeducted.builder()
                .orderId(orderInventoryDto.orderId())
                .inventoryId(orderInventoryDto.inventoryId())
                .productId(orderInventoryDto.productId())
                .quantity(orderInventoryDto.quantity())
                .createdAt(Instant.now())
                .build();
    }

    public static InventoryEvent toInventoryRestoredEvent(OrderInventoryDto orderInventoryDto){
        return InventoryEvent.InventoryRestored.builder()
                .orderId(orderInventoryDto.orderId())
                .inventoryId(orderInventoryDto.inventoryId())
                .productId(orderInventoryDto.productId())
                .quantity(orderInventoryDto.quantity())
                .createdAt(Instant.now())
                .build();
    }

    public static Function<Throwable, Mono<InventoryEvent>> toInventoryDeclinedEvent(OrderEvent.OrderCreated event){
        return ex -> Mono.fromSupplier(() -> InventoryEvent.InventoryDeclined.builder()
                .orderId(event.orderId())
                .productId(event.productId())
                .quantity(event.quantity())
                .createdAt(Instant.now())
                .message(ex.getMessage())
                .build()

        );
    }
}
