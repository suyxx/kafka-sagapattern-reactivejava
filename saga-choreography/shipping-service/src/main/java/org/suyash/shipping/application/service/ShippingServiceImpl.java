package org.suyash.shipping.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.suyash.common.events.shipping.ShippingStatus;
import org.suyash.shipping.application.entity.Shipment;
import org.suyash.shipping.application.mapper.EntityDtoMapper;
import org.suyash.shipping.application.repository.ShipmentRepository;
import org.suyash.shipping.common.dto.ScheduleRequest;
import org.suyash.shipping.common.dto.ShipmentDto;
import org.suyash.shipping.common.service.ShippingService;
import org.suyash.util.DuplicateEventValidator;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {

    private final ShipmentRepository repository;

    @Override
    public Mono<Void> addShipment(ScheduleRequest request) {
        return DuplicateEventValidator.validate(
                this.repository.existsByOrderId(request.orderId()),
                Mono.defer(() -> this.add(request))
        );
    }

    private Mono<Void> add(ScheduleRequest request) {
        var shipment = EntityDtoMapper.toShipment(request);
        shipment.setStatus(ShippingStatus.PENDING);
        return this.repository.save(shipment)
                              .then();
    }

    @Override
    public Mono<Void> cancel(UUID orderId) {
        return this.repository.deleteByOrderId(orderId);
    }

    @Override
    public Mono<ShipmentDto> schedule(UUID orderId) {
        return this.repository.findByOrderIdAndStatus(orderId, ShippingStatus.PENDING)
                              .flatMap(this::schedule);
    }

    private Mono<ShipmentDto> schedule(Shipment shipment) {
        shipment.setDeliveryDate(Instant.now().plus(Duration.ofDays(3)));
        shipment.setStatus(ShippingStatus.SCHEDULED);
        return this.repository.save(shipment)
                              .map(EntityDtoMapper::toDto);
    }

}
