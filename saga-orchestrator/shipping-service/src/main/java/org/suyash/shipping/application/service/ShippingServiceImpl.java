package org.suyash.shipping.application.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.common.messages.shipping.ShippingStatus;
import org.suyash.common.util.DuplicateEventValidator;
import org.suyash.shipping.application.entity.Shipment;
import org.suyash.shipping.application.mapper.EntityDtoMapper;
import org.suyash.shipping.application.repository.ShipmentRepository;
import org.suyash.shipping.common.dto.ScheduleRequest;
import org.suyash.shipping.common.dto.ShipmentDto;
import org.suyash.shipping.common.exception.ShipmentQuantityLimitExceededException;
import org.suyash.shipping.common.service.ShippingService;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

    private static final Mono<Shipment> LIMIT_EXCEEDED = Mono.error(new ShipmentQuantityLimitExceededException());
    private final ShipmentRepository repository;

    @Override
    public Mono<ShipmentDto> schedule(ScheduleRequest request) {
        return DuplicateEventValidator.validate(
                                              this.repository.existsByOrderId(request.orderId()),
                                              Mono.just(request)
                                      )
                                      .filter(r -> r.quantity() < 10)
                                      .map(EntityDtoMapper::toShipment)
                                      .switchIfEmpty(LIMIT_EXCEEDED)
                                      .flatMap(this::schedule);
    }

    private Mono<ShipmentDto> schedule(Shipment shipment) {
        shipment.setDeliveryDate(Instant.now().plus(Duration.ofDays(3)));
        shipment.setStatus(ShippingStatus.SCHEDULED);
        return this.repository.save(shipment)
                              .map(EntityDtoMapper::toDto);
    }

}
