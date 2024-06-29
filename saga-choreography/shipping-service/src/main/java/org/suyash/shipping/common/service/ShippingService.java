package org.suyash.shipping.common.service;


import org.suyash.shipping.common.dto.ScheduleRequest;
import org.suyash.shipping.common.dto.ShipmentDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ShippingService {

    Mono<Void> addShipment(ScheduleRequest request);

    Mono<Void> cancel(UUID orderId);

    Mono<ShipmentDto> schedule(UUID orderId);

}

