package org.suyash.order.messaging.mapper;

import org.suyash.common.events.shipping.ShippingEvent;
import org.suyash.order.common.dto.OrderShipmentSchedule;

public class ShippingEventMapper {

    public static OrderShipmentSchedule toDto(ShippingEvent.ShippingScheduled event) {
        return OrderShipmentSchedule.builder()
                                       .orderId(event.orderId())
                                       .deliveryDate(event.expectedDelivery())
                                       .build();
    }

}
