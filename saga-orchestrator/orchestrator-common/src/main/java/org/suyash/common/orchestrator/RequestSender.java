package org.suyash.common.orchestrator;

import org.reactivestreams.Publisher;
import org.suyash.common.messages.Request;

import java.util.UUID;

public interface RequestSender {

    Publisher<Request> send(UUID id);

}
