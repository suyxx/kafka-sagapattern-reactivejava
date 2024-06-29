package org.suyash.inventory.application.mapper;

import org.suyash.inventory.application.entity.OrderInventory;
import org.suyash.inventory.common.dto.InventoryDeductRequest;
import org.suyash.inventory.common.dto.OrderInventoryDto;

public class EntityDtoMapper {


    public static OrderInventory toOrderInventory(InventoryDeductRequest request){
        return OrderInventory.builder()
                .orderId(request.orderId())
                .productId(request.productId())
                .quantity(request.quality())
                .build();
    }

    public static OrderInventoryDto toDto(OrderInventory orderInventory){
        return OrderInventoryDto.builder()
                .inventoryId(orderInventory.getInventoryId())
                .orderId(orderInventory.getOrderId())
                .productId(orderInventory.getProductId())
                .quantity(orderInventory.getQuantity())
                .status(orderInventory.getStatus())
                .build();
    }

}
