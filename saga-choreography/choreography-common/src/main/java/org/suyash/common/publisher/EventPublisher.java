package org.suyash.common.publisher;

import org.suyash.common.events.DomainEvent;
import reactor.core.publisher.Flux;

public interface EventPublisher <T extends DomainEvent> {
    Flux<T> publish();
}
