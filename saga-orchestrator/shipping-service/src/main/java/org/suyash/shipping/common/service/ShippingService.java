package org.suyash.shipping.common.service;
import org.suyash.shipping.common.dto.ScheduleRequest;
import org.suyash.shipping.common.dto.ShipmentDto;
import reactor.core.publisher.Mono;

public interface ShippingService {

    Mono<ShipmentDto> schedule(ScheduleRequest request);

}

