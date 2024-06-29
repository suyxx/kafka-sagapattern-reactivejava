package org.suyash.common.processor;

import org.suyash.common.messages.Request;
import org.suyash.common.messages.Response;
import reactor.core.publisher.Mono;

public interface RequestProcessor<T extends Request, R extends Response> {

    Mono<R> process(T request);

}
