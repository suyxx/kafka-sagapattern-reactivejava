package org.suyash.inventory.common.service;

import org.suyash.common.events.inventory.InventoryStatus;
import org.suyash.inventory.common.dto.InventoryDeductRequest;
import org.suyash.inventory.common.dto.OrderInventoryDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface InventoryService {

    Mono<OrderInventoryDto> deduct(InventoryDeductRequest request);
    Mono<OrderInventoryDto> restore(UUID orderId);

}
