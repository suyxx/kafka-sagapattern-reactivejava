package org.suyash.order.common.service;


import org.suyash.order.common.dto.PurchaseOrderDto;

public interface OrderEventListener {

    void emitOrderCreated(PurchaseOrderDto dto);

}
